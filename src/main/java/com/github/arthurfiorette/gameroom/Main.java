package com.github.arthurfiorette.gameroom;

import com.github.arthurfiorette.gameroom.config.BotConfig;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;

public class Main {

  public static JDA jda;

  public static void main(String[] args) throws LoginException {
    BotConfig config = new BotConfig(".properties");
    config.read();

    Gameroom gr = new Gameroom(config);
    gr.start();
  }
}
