package com.github.arthurfiorette.gameroom.old.room;

import com.github.arthurfiorette.gameroom.old.Gameroom;
import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

@UtilityClass
public class RoomEmbed {

  public static MessageEmbed create() {
    final EmbedBuilder builder = new EmbedBuilder();

    builder.setColor(Gameroom.MAIN_COLOR);
    builder.addField("", "Reaja com 🎮 para criar sua sala", false);

    return builder.build();
  }
}
