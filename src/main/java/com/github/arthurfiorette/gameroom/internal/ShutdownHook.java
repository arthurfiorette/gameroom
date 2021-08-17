package com.github.arthurfiorette.gameroom.internal;

import com.github.arthurfiorette.gameroom.Gameroom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShutdownHook extends Thread {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Getter
  @NonNull
  private final Gameroom gameroom;

  @Override
  public void run() {
    logger.warn("Shuting down the JDA instance.");
    gameroom.getJda().shutdown();
    logger.warn("JDA was shut down");
  }
}
