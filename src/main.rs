use serenity::{framework::StandardFramework, Client};
use std::env;

mod listener;

#[tokio::main]
async fn main() {
  // Load .env file
  dotenv::dotenv().expect("Failed to load .env file");

  let token = env::var("DISCORD_TOKEN").expect("Expected a token in the environment");

  let framework = StandardFramework::new().configure(|c| c.prefix("$"));

  let mut client = Client::builder(token)
    .event_handler(listener::Listener)
    .framework(framework)
    .await
    .expect("Error while creating discord client");

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
