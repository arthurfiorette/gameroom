use serenity::framework::standard::macros::group;

mod ping;
use ping::*;

#[group]
#[commands(ping)]
pub struct GeneralCommands;
