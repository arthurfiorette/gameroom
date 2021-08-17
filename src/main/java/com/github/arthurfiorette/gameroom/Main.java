package com.github.arthurfiorette.gameroom;

import com.github.arthurfiorette.gameroom.config.BotConfig;

public final class Main {

  public static void main(String[] args) {
    final BotConfig config = new BotConfig(".properties");
    config.read();

    final Gameroom gameroom = new Gameroom(config);
    gameroom.create();
  }
}
