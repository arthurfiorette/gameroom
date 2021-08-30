package com.github.arthurfiorette.gameroom;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;

@Deprecated
public class GuildReadyListener extends ListenerAdapter {

  private final Set<Long> messagesIds = new HashSet<>();

  @Override
  public void onGuildReady(final GuildReadyEvent event) {
    // Delete all others categories name gamerooms
    final Guild guild = event.getGuild();
    deleteGamerooms(guild);

    // Create the category
    final CompletableFuture<Category> action = guild.createCategory("gamerooms").submit();

    action.thenAcceptAsync(category -> {
      final TextChannel text = category.createTextChannel("gameroom").complete();

      // Send the message to the text channel
      final Message msg = text.sendMessage("Reaja com qualquer emoji para criar o seu").complete();
      msg.addReaction("U+1F4A2").queue();
      messagesIds.add(msg.getIdLong());
    });
  }

  @Override
  public void onMessageReactionAdd(final MessageReactionAddEvent event) {
    final long id = event.getMessageIdLong();
    final User user = event.getUser();

    if (!messagesIds.contains(id) || user.isBot()) {
      return;
    }

    final CompletableFuture<Void> action = event.getReaction().removeReaction(user).submit();

    action.thenRunAsync(() -> {
      final Member member = event.getMember();
      final Guild guild = event.getGuild();

      final Category cat = event.getTextChannel().getParent();
      final TextChannel ch = cat
        .createTextChannel("gameroom-" + user.getName())
        .addPermissionOverride(member, EnumSet.of(Permission.VIEW_CHANNEL), null)
        .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
        .complete();
      ch.sendMessage(member.getAsMention()).complete();

      cat
        .createVoiceChannel("gameroom-" + user.getName())
        .addPermissionOverride(member, EnumSet.of(Permission.VIEW_CHANNEL), null)
        .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
        .complete();
    });
  }

  private void deleteGamerooms(final Guild guild) {
    RestAction<Void> action = null;

    for (final GuildChannel channel : guild.getChannels()) {
      if (channel.getName().startsWith("gameroom")) {
        if (action != null) {
          action = action.and(channel.delete());
          continue;
        }

        action = channel.delete();
      }
    }

    if (action != null) {
      action.complete();
    }
  }
}
