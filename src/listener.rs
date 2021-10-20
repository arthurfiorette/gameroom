use serenity::model::prelude::{Activity, OnlineStatus, Ready, ResumedEvent};
use serenity::{async_trait, client::EventHandler, model::channel::Message, prelude::*};

pub struct Listener;

#[async_trait]
impl EventHandler for Listener {
  async fn ready(&self, ctx: Context, ready: Ready) {
    println!("Connected as {}", ready.user.name);

    // Discord rich presence
    ctx
      .set_presence(
        Some(Activity::listening("some private room!")),
        OnlineStatus::Online,
      )
      .await;
  }

  async fn resume(&self, _: Context, _: ResumedEvent) {
    println!("Resumed");
  }
}
