package com.github.arthurfiorette.gameroom.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public enum Property {
  AUTH_TOKEN("auth.token"),
  SHARD_COUNT("shard.count"),
  MONGO_CONNECTION("mongodb.connection-string"),
  MONGO_DATABASE("mongodb.database-name"),
  CUSTOMIZATION_CATEGORY_NAME("customization.category.name");

  @Getter
  @NonNull
  private final String path;
}
