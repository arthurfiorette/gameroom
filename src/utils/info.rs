use serenity::{http::client::Http, model::id::UserId};
use std::collections::HashSet;

pub async fn get_owners_and_bot_id(http: &Http) -> (HashSet<UserId>, UserId) {
  match http.get_current_application_info().await {
    Err(why) => panic!("Could not access the bot application info: {:?}", why),
    Ok(info) => {
      let mut owners = HashSet::new();

      if let Some(team) = info.team {
        owners.insert(team.owner_user_id);
      } else {
        owners.insert(info.owner.id);
      }

      match http.get_current_user().await {
        Ok(bot) => (owners, bot.id),
        Err(why) => panic!("Could not access the bot current user: {:?}", why),
      }
    }
  }
}

pub fn create_http_client(token: &str) -> Http {
  Http::new_with_token(token)
}
