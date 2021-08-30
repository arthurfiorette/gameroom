package com.github.arthurfiorette.gameroom.listeners;

import com.github.arthurfiorette.gameroom.data.model.CreatedRoom;
import com.github.arthurfiorette.gameroom.data.model.GuildConfig;
import com.github.arthurfiorette.gameroom.shard.ShardInstance;
import dev.morphia.Datastore;
import java.util.EnumSet;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

@RequiredArgsConstructor
public class GuildListener {

  @NonNull
  private final ShardInstance shard;

  @NonNull
  private final GuildConfig config;

  @SubscribeEvent
  public void onMessageReactionAdd(final GuildMessageReactionAddEvent event) {
    final Guild guild = event.getGuild();
    final TextChannel channel = event.getChannel();
    final User user = event.getUser();
    final Category category = channel.getParent();

    if (
      // Filter correct channel
      guild.getIdLong() != config.getGuildId() ||
      channel.getIdLong() != config.getConfigChannelId() ||
      event.getMessageIdLong() != config.getConfigMessageId() ||
      // Invalid users
      user.isBot() ||
      user.isSystem()
    ) {
      return;
    }

    final CreatedRoom room = new CreatedRoom(config);
    final Member member = event.getMember();

    // Remove the action
    event.getReaction().removeReaction(user).queue();

    // Create the text and voice channel
    final TextChannel textChannel = category
      .createTextChannel("gameroom-" + user.getName())
      .addPermissionOverride(member, EnumSet.of(Permission.VIEW_CHANNEL), null)
      .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
      .complete();
    textChannel.sendMessage(member.getAsMention()).complete();

    final VoiceChannel voiceChannel = category
      .createVoiceChannel("gameroom-" + user.getName())
      .addPermissionOverride(member, EnumSet.of(Permission.VIEW_CHANNEL), null)
      .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
      .complete();

    room.setTextChannelId(textChannel.getIdLong());
    room.setTextChannelId(voiceChannel.getIdLong());

    // Add the room to the list
    final Datastore datastore = shard.getGameroom().getDatastore();
    datastore.save(room);
    config.getRooms().add(room);
    datastore.save(config);
  }
}
