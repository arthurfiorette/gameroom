package com.github.arthurfiorette.gameroom.old.room;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

@RequiredArgsConstructor
public class GameRoom {

  @Getter
  @NonNull
  private final TextChannel textChannel;

  @Getter
  @NonNull
  private final VoiceChannel voiceChannel;

  public void prepareTextChannel() {}
}
