package com.github.arthurfiorette.gameroom.data.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import dev.morphia.annotations.Reference;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.bson.types.ObjectId;

@Data
@Entity
@NoArgsConstructor
@FieldNameConstants
public class GuildConfig {

  @Id
  private ObjectId id;

  @Property
  private long guildId;

  @Property
  private long categoryId;

  @Property
  private long configChannelId;

  @Property
  private long configMessageId;

  @Property
  private boolean configured;

  @Reference
  private List<CreatedRoom> rooms = new ArrayList<>();

  public GuildConfig(long guildId) {
    this.guildId = guildId;
    this.configured = false;
  }
}
