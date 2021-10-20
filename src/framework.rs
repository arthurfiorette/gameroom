use serenity::client::Context;
use serenity::framework::standard::macros::hook;
use serenity::framework::standard::{CommandResult, DispatchError, StandardFramework};
use serenity::http::Http;
use serenity::model::channel::Message;

use super::commands::*;
use super::utils;

pub async fn create_framework(http_client: &Http) -> StandardFramework {
  let (owners, bot_id) = utils::get_owners_and_bot_id(http_client).await;

  StandardFramework::new()
    .configure(|c| {
      c.with_whitespace(true)
        .on_mention(Some(bot_id))
        .owners(owners)
        .prefix("!")
    })
    // .before(before_handler)
    .after(after_handler)
    .on_dispatch_error(dispatch_error)
    .unrecognised_command(unknown_command)
    .group(&GENERALCOMMANDS_GROUP)
}

#[hook]
pub async fn unknown_command(ctx: &Context, msg: &Message, name: &str) {
  match msg
    .reply(
      ctx,
      format!("I couldn't found a command name called `{}`", name),
    )
    .await
  {
    Err(why) => println!("Error sending unknown_command message: {:?}", why),
    _ => (),
  }
}

// #[hook]
// async fn before_handler(_ctx: &Context, msg: &Message, command_name: &str) -> bool {
//   println!("Message: {0}. Command: {1}", msg.content, command_name);

//   true
// }

#[hook]
async fn after_handler(
  _ctx: &Context,
  _msg: &Message,
  command_name: &str,
  command_result: CommandResult,
) {
  match command_result {
    Ok(()) => println!("Processed command '{}'", command_name),
    Err(why) => println!("Command '{}' returned error {:?}", command_name, why),
  }
}

#[hook]
async fn dispatch_error(ctx: &Context, msg: &Message, error: DispatchError) {
  if let DispatchError::Ratelimited(info) = error {
    // We notify them only once.
    if info.is_first_try {
      let _ = msg
        .channel_id
        .say(
          &ctx.http,
          &format!("Try this again in {} seconds.", info.as_secs()),
        )
        .await;
    }
  }
}
