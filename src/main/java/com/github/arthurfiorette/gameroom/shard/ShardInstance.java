package com.github.arthurfiorette.gameroom.shard;

import com.github.arthurfiorette.gameroom.Gameroom;
import com.github.arthurfiorette.gameroom.listeners.GuildReadyListener;
import com.google.common.collect.ImmutableList;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.requests.RestAction;

@Slf4j
@Getter
@RequiredArgsConstructor
public class ShardInstance {

  private final int id;

  private final Gameroom gameroom;

  private final ImmutableList<Object> shardListeners = ImmutableList.of();

  @Setter(value = AccessLevel.PACKAGE)
  private JDA nullableJda;

  private final GuildReadyListener readyListener = new GuildReadyListener(this);

  public JDA getJda() {
    return Objects.requireNonNull(nullableJda, "Shard is not ready yet");
  }

  public boolean hasStarted() {
    return nullableJda != null;
  }

  @SubscribeEvent
  public void onReady(@NonNull final ReadyEvent event) {
    final JDA jda = event.getJDA();
    setNullableJda(jda);

    jda.getPresence().setActivity(Activity.playing("Shard Id: " + id));
    ShardInstance.log.info("Shard {} is ready", id);

    // Delete all channels (TEST ONLY)
    // deleteGamerooms(jda.getGuilds().get(0));
  }

  void deleteGamerooms(final Guild guild) {
    RestAction<Void> action = null;

    for (final Category cat : guild.getCategories()) {
      if (action != null) {
        action = action.and(cat.delete());
        continue;
      }

      action = cat.delete();
    }

    for (final GuildChannel channel : guild.getChannels()) {
      if (action != null) {
        action = action.and(channel.delete());
        continue;
      }

      action = channel.delete();
    }

    if (action != null) {
      action.complete();
    }
  }
}
