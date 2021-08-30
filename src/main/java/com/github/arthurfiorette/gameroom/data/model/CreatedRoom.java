package com.github.arthurfiorette.gameroom.data.model;

import java.time.Instant;

import org.bson.types.ObjectId;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import dev.morphia.annotations.Reference;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class CreatedRoom {

  @Id
  private ObjectId id;

  @Reference
  private GuildConfig config;

  @Property
  private long textChannelId;

  @Property
  private long voiceChannelId;

  @Property
  private Instant createdAt;

  public CreatedRoom(GuildConfig config) {
    this.config = config;
    this.createdAt = Instant.now();
  }
}
