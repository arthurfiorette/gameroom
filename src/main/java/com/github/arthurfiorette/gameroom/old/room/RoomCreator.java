package com.github.arthurfiorette.gameroom.old.room;

import com.github.arthurfiorette.gameroom.old.Gameroom;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@RequiredArgsConstructor
public class RoomCreator extends ListenerAdapter {

  @Getter
  @NonNull
  private final Gameroom gameroom;
  
  @Override
  public void onGuildReady(GuildReadyEvent event) {
    Guild guild = event.getGuild();

    Category category = guild.createCategory("GameRooms").complete();
    GameRoom room = createGameRoom(category, 1);
  }

  public GameRoom createGameRoom(Category category, int id) {
    TextChannel text = category.createTextChannel("gameroom-" + id).complete();
    VoiceChannel voice = category.createVoiceChannel("gameroom-" + id).complete();
    category.getManager();
    text.sendMessageEmbeds(RoomEmbed.create()).queue();
    
    return new GameRoom(text, voice);
  }

}
