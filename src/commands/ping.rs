use serenity::framework::standard::{macros::command, CommandResult};
use serenity::model::prelude::*;
use serenity::prelude::*;

use crate::utils::now;

#[command]
#[only_in(guilds)]
pub async fn ping(ctx: &Context, msg: &Message) -> CommandResult {
  let ts = msg.timestamp.timestamp_millis() as u128; // i64 to u128
  let now = now();

  msg
    .channel_id
    .say(&ctx.http, format!("Pong! `~{:?}ms`", now - ts))
    .await?;

  Ok(())
}
