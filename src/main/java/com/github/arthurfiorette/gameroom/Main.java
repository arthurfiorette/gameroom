package com.github.arthurfiorette.gameroom;

import com.github.arthurfiorette.gameroom.config.BotConfig;
import com.github.arthurfiorette.gameroom.config.Property;
import com.github.arthurfiorette.gameroom.old.room.RoomCreator;
import java.util.Arrays;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Main {

  public static JDA jda;

  public static void main(String[] args) throws LoginException {
    BotConfig config = new BotConfig(".properties");
    config.read();

    String token = config.get(Property.AUTH_TOKEN);

    jda =
      JDABuilder
        .create(token, Arrays.asList(Constants.INTENTS))
        .setMemberCachePolicy(MemberCachePolicy.ALL)
        .enableCache(Arrays.asList(Constants.CACHE_FLAGS))
        .setStatus(OnlineStatus.ONLINE)
        .addEventListeners(new GuildReadyListener())
        .setActivity(Activity.streaming("Tribo gaules", "https://twitch.tv/gaules"))
        .build();
  }
}
