package com.github.arthurfiorette.gameroom;

import javax.security.auth.login.LoginException;

import com.github.arthurfiorette.gameroom.config.BotConfig;

import net.dv8tion.jda.api.JDA;

public class Main {

  public static JDA jda;

  public static void main(final String[] args) throws LoginException {
    final BotConfig config = new BotConfig(".properties");
    config.read();

    final Gameroom gr = new Gameroom(config);
    gr.prepareJda();
    gr.prepareDb();

    gr.start();
  }
}
