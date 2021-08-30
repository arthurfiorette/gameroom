package com.github.arthurfiorette.gameroom.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;

/**
 * An {@link AnnotatedEventManager} that executes all listener methods in a asynchronous manner.
 * This allows all listeners no use complete() without blocking any other execution.
 */
@AllArgsConstructor
public class AsyncAnnotatedEventManager extends AnnotatedEventManager {

  @Getter
  private final ExecutorService threadPool = Executors.newCachedThreadPool();

  @Override
  public void handle(@Nonnull final GenericEvent event) {
    threadPool.execute(() -> super.handle(event));

    // Shutdown automatically
    if (event instanceof ShutdownEvent) {
      threadPool.shutdown();
    }
  }
}
