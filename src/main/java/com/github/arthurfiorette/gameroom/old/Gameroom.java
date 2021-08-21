package com.github.arthurfiorette.gameroom.old;

import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MEMBERS;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGES;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGE_REACTIONS;

import com.github.arthurfiorette.gameroom.config.BotConfig;
import com.github.arthurfiorette.gameroom.config.Property;
import com.github.arthurfiorette.gameroom.old.internal.ShutdownHook;
import com.github.arthurfiorette.gameroom.old.room.RoomCreator;
import java.awt.Color;
import java.util.Arrays;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

@RequiredArgsConstructor
public final class Gameroom {

  public static final Color MAIN_COLOR = new Color(0xF1443F);

  private static final GatewayIntent[] INTENTS = {
    GUILD_MESSAGE_REACTIONS,
    GUILD_MESSAGES,
    GUILD_MEMBERS,
  };

  private static final CacheFlag[] CACHE_FLAGS = {};

  @Getter
  @NonNull
  private final BotConfig config;

  @Getter
  private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

  @Getter
  private JDA jda;

  @SneakyThrows
  public void create() {
    this.jda = createJda().build();
    Runtime.getRuntime().addShutdownHook(new ShutdownHook(this));
  }

  private JDABuilder createJda() {
    final String token = config.get(Property.AUTH_TOKEN);

    return JDABuilder
      .create(token, Arrays.asList(INTENTS))
      .setMemberCachePolicy(MemberCachePolicy.ALL)
      .enableCache(Arrays.asList(CACHE_FLAGS))
      .setStatus(OnlineStatus.ONLINE)
      .addEventListeners(new RoomCreator(this))
      .setActivity(Activity.streaming("Tribo gaules", "https://twitch.tv/gaules"));
  }
}
