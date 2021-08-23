package com.github.arthurfiorette.gameroom.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public enum Property {
  AUTH_TOKEN("auth.token", true),
  SHARD_COUNT("shard.count", true);

  @Getter
  @NonNull
  private final String path;

  @Getter
  private final boolean required;

}
