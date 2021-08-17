package com.github.arthurfiorette.gameroom.activity;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.RichPresence;

public class CustomActivity implements Activity {

  @Override
  public boolean isRich() {
    return false;
  }

  @Override
  public RichPresence asRichPresence() {
    return null;
  }

  @Override
  public String getName() {
    return "asasdd";
  }

  @Override
  public String getUrl() {
    return "https://www.twitch.tv/hazork_";
  }

  @Override
  public ActivityType getType() {
   return ActivityType.CUSTOM_STATUS;
  }

  @Override
  public Timestamps getTimestamps() {
    return new Timestamps(System.currentTimeMillis(), System.currentTimeMillis() +  1000 * 60 * 5);
  }

  @Override
  public Emoji getEmoji() {
    return new Emoji("a");
  }

}
