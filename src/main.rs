use serenity::client::bridge::gateway::{GatewayIntents, ShardManager};
use serenity::prelude::*;
use std::env;
use std::sync::Arc;

mod commands;
mod framework;
mod listener;
mod utils;

struct ShardManagerContainer;
impl TypeMapKey for ShardManagerContainer {
  type Value = Arc<Mutex<ShardManager>>;
}

#[tokio::main]
async fn main() {
  // Load .env file
  dotenv::dotenv().expect("Failed to load .env file");

  let token = env::var("DISCORD_TOKEN").expect("Expected a token in the environment");
  let http_client = utils::create_http_client(&token);
  let framework = framework::create_framework(&http_client).await;

  let mut client = Client::builder(token)
    .event_handler(listener::Listener)
    .framework(framework)
    .intents(GatewayIntents::all())
    .await
    .expect("Error while creating discord client");

  {
    let mut data = client.data.write().await;
    data.insert::<ShardManagerContainer>(Arc::clone(&client.shard_manager));
  }

  if let Err(err) = client.start().await {
    println!("An error occurred: {:?}", err);
  }

  let shard_manager = client.shard_manager.clone();

  tokio::spawn(async move {
    tokio::signal::ctrl_c()
      .await
      .expect("Could not register ctrl+c handler");
    shard_manager.lock().await.shutdown_all().await;
  });
}
