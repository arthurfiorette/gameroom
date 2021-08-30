package com.github.arthurfiorette.gameroom.listeners;

import com.github.arthurfiorette.gameroom.config.BotConfig;
import com.github.arthurfiorette.gameroom.config.Property;
import com.github.arthurfiorette.gameroom.data.model.GuildConfig;
import com.github.arthurfiorette.gameroom.shard.ShardInstance;
import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filters;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

@Slf4j
@RequiredArgsConstructor
public class GuildReadyListener {

  @Getter
  @NonNull
  private final ShardInstance shard;

  @SubscribeEvent
  public void onGuildReady(final GuildReadyEvent event) {
    final Datastore datastore = shard.getGameroom().getDatastore();
    final Guild guild = event.getGuild();

    GuildConfig config = datastore
      .find(GuildConfig.class)
      .filter(Filters.eq(GuildConfig.Fields.guildId, guild.getIdLong()))
      .first();

    if (config == null || !config.isConfigured()) {
      config = createGuildConfig(guild);
    }

    // Add his dedicated listener
    shard.getJda().addEventListener(new GuildListener(shard, config));
  }

  private GuildConfig createGuildConfig(final Guild guild) {
    final GuildConfig config = new GuildConfig(guild.getIdLong());
    final Datastore datastore = shard.getGameroom().getDatastore();
    final BotConfig botConfig = shard.getGameroom().getConfig();

    log.info("Configuring guild {}.", guild.getId());

    final String name = botConfig.get(Property.CUSTOMIZATION_CATEGORY_NAME);
    final Category category = guild.createCategory(name).complete();
    final TextChannel text = category.createTextChannel("gameroom").complete();
    final Message message = text.sendMessage("Reaja com ⤴ para criar o seu canal.").complete();

    log.info("Category {} ready with its needs", category.getIdLong());

    message.addReaction("U+2934").submit();

    config.setCategoryId(category.getIdLong());
    config.setConfigChannelId(text.getIdLong());
    config.setConfigMessageId(message.getIdLong());
    config.setConfigured(true);

    datastore.save(config);

    log.info(
      "Guild {} set up and configured with category {}",
      guild.getIdLong(),
      category.getId()
    );

    return config;
  }
}
