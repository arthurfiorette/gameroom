package com.github.arthurfiorette.gameroom.shard;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.github.arthurfiorette.gameroom.Gameroom;
import com.github.arthurfiorette.gameroom.config.BotConfig;
import com.github.arthurfiorette.gameroom.config.Property;
import com.github.arthurfiorette.gameroom.util.AsyncAnnotatedEventManager;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;

@RequiredArgsConstructor
public class ShardFactory {

  @Getter
  @NonNull
  private final Gameroom gameroom;

  @Getter
  private final ConcurrentMap<Integer, ShardInstance> shards = new ConcurrentHashMap<>();

  public DefaultShardManagerBuilder applyShardsConfig(
    @NonNull final BotConfig config,
    @NonNull final DefaultShardManagerBuilder builder
  ) {
    // Per instance event manager
    builder.setEventManagerProvider(id -> new AsyncAnnotatedEventManager());
    builder.addEventListenerProviders(ShardingUtils.getJdaListeners(this::getOrCreate));
    builder.setShardsTotal(config.getInt(Property.SHARD_COUNT, -1));

    return builder;
  }

  public ShardInstance getOrCreate(final int id) {
    return shards.computeIfAbsent(
      id,
      key -> {
        final ShardInstance instance = new ShardInstance(id, gameroom);
        instance.getShardListeners().forEach(gameroom.getEventBus()::register);
        return instance;
      }
    );
  }

  public ShardInstance getOf(@NonNull final JDA jda) {
    return shards.get(jda.getShardInfo().getShardId());
  }
}
