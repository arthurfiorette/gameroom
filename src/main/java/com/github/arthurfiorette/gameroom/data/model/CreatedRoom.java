package com.github.arthurfiorette.gameroom.data.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import dev.morphia.annotations.Reference;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

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
