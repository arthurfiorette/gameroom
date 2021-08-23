package com.github.arthurfiorette.gameroom.shard;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.github.arthurfiorette.gameroom.Gameroom;
import com.github.arthurfiorette.gameroom.config.BotConfig;
import com.github.arthurfiorette.gameroom.config.Property;

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
  private ConcurrentMap<Integer, ShardInstance> shards = new ConcurrentHashMap<>();

  public ShardInstance getOrCreate(int id) {
    return shards.computeIfAbsent(
      id,
      key -> {
        System.out.println(key);

        return create(key);
      }
    );
  }

  public ShardInstance getOf(@NonNull final JDA jda) {
    return shards.get(jda.getShardInfo().getShardId());
  }

  public DefaultShardManagerBuilder applyShardsConfig(
    @NonNull final BotConfig config,
    @NonNull final DefaultShardManagerBuilder builder
  ) {
    // Per instance event manager
    builder.setEventManagerProvider(id -> getOrCreate(id).getEventManager());
    builder.addEventListenerProviders(ShardInstance.getJdaListeners(this::getOrCreate));
    builder.setShardsTotal(config.getInt(Property.SHARD_COUNT));

    return builder;
  }

  private ShardInstance create(int id) {
    ShardInstance instance = new ShardInstance(id, gameroom);

    instance.getShardListeners().forEach(gameroom.getEventBus()::register);
    
    return instance;
  }
}
