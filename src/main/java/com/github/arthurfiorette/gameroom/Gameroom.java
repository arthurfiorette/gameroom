package com.github.arthurfiorette.gameroom;

import com.github.arthurfiorette.gameroom.config.BotConfig;
import com.github.arthurfiorette.gameroom.config.MongoManager;
import com.github.arthurfiorette.gameroom.config.Property;
import com.github.arthurfiorette.gameroom.shard.ShardFactory;
import com.google.common.eventbus.EventBus;
import dev.morphia.Datastore;
import java.awt.Color;
import java.util.Collection;
import java.util.EnumSet;
import java.util.stream.Collectors;
import javax.security.auth.login.LoginException;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class Gameroom {

  private static final Logger log = LoggerFactory.getLogger(Gameroom.class);

  public static final Color MAIN_COLOR = new Color(0xF1443F);

  @Getter
  @NonNull
  private final BotConfig config;

  @Getter
  private final ShardFactory shardFactory = new ShardFactory(this);

  @Getter
  private final MongoManager mongoManager = new MongoManager(this);

  @Getter
  private ShardManager shardManager;

  @Getter
  // To communicate between shards
  private final EventBus eventBus = new EventBus();

  @SneakyThrows(LoginException.class)
  public void prepareJda() throws IllegalArgumentException {
    log.info("Setting up JDA");
    log.info(
      "Using intents {}",
      gatewayIntents().stream().map(Enum::name).collect(Collectors.joining(", "))
    );
    log.info(
      "Disabling cache for {}",
      disableCacheFlags().stream().map(Enum::name).collect(Collectors.joining(", "))
    );
    log.info("Using {} shards", config.getInt(Property.SHARD_COUNT, -1));

    final DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.create(
      config.get(Property.AUTH_TOKEN),
      gatewayIntents()
    );

    builder.setChunkingFilter(ChunkingFilter.NONE);
    builder.disableCache(disableCacheFlags());
    builder.setActivity(Activity.playing("Hold on..."));
    builder.setAutoReconnect(true);

    shardFactory.applyShardsConfig(config, builder);

    log.info("JDA is ready to start.");

    shardManager = builder.build(false);
  }

  public void prepareDb() {
    mongoManager.createClient();
  }

  public void start() throws LoginException {
    mongoManager.connect();
    shardManager.login();
  }

  private Collection<GatewayIntent> gatewayIntents() {
    return EnumSet.of(
      // Listen to messages and its reactions, so we can use reaction menus
      // and etc
      GatewayIntent.GUILD_MESSAGES,
      GatewayIntent.GUILD_MESSAGE_REACTIONS,
      // Track presence to cleanup unused rooms
      GatewayIntent.GUILD_MEMBERS
    );
  }

  private Collection<CacheFlag> disableCacheFlags() {
    return EnumSet.of(
      CacheFlag.ACTIVITY,
      CacheFlag.EMOTE,
      CacheFlag.VOICE_STATE,
      CacheFlag.CLIENT_STATUS,
      CacheFlag.ROLE_TAGS,
      CacheFlag.ONLINE_STATUS
    );
  }

  public Datastore getDatastore() {
    return this.getMongoManager().getDatastore();
  }
}
