package com.github.arthurfiorette.gameroom.shard;

import com.github.arthurfiorette.gameroom.Gameroom;
import com.google.common.collect.ImmutableList;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import java.util.Collection;
import java.util.Objects;
import java.util.function.IntFunction;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.hooks.IEventManager;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

@RequiredArgsConstructor
public class ShardInstance {

  private static final Logger log = JDALogger.getLog(ShardInstance.class);

  @Getter
  private final int id;

  @Getter
  @NonNull
  private final Gameroom gameroom;

  @Getter
  private final IEventManager eventManager = new AnnotatedEventManager();

  @Getter
  private final ImmutableList<Object> shardListeners = ImmutableList.of();

  @Getter
  private final EventWaiter eventWaiter = new EventWaiter();

  @Getter
  @Setter(value = AccessLevel.PACKAGE)
  private JDA nullableJda;

  public JDA getJda() {
    return Objects.requireNonNull(nullableJda, "Shard is not ready yet");
  }

  public boolean hasStarted() {
    return this.nullableJda != null;
  }

  @SubscribeEvent
  public void onReady(@NonNull final ReadyEvent event) {
    final JDA jda = event.getJDA();
    setNullableJda(jda);

    jda.getPresence().setActivity(Activity.playing("Shard Id: " + id));
    log.info("Shard {} is ready", id);
  }

  /**
   * Generate a int function list for every shard id needed.
   */
  public static Collection<IntFunction<Object>> getJdaListeners(
    IntFunction<ShardInstance> generator
  ) {
    return ImmutableList.of(
      // shard instance itself
      generator::apply,
      id -> generator.apply(id).getEventWaiter()
    );
  }

  public static IEventManager getEventManager(int shardId) {
    return new AnnotatedEventManager();
  }
}
