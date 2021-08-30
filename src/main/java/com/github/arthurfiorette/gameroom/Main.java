package com.github.arthurfiorette.gameroom;

import com.github.arthurfiorette.gameroom.config.BotConfig;
import javax.security.auth.login.LoginException;
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
