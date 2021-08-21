package com.github.arthurfiorette.gameroom;

import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MEMBERS;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGES;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGE_REACTIONS;

import java.awt.Color;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Constants {

  public static final Color MAIN_COLOR = new Color(0xF1443F);

  public static final GatewayIntent[] INTENTS = {
    GUILD_MESSAGE_REACTIONS,
    GUILD_MESSAGES,
    GUILD_MEMBERS,
  };

  public static final CacheFlag[] CACHE_FLAGS = {};
}
