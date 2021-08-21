package com.github.arthurfiorette.gameroom.old.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Property {

  AUTH_TOKEN("auth.token");

  @Getter
  @NonNull
  private final String path;
}
