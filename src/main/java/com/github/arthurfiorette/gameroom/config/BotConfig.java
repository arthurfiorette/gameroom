package com.github.arthurfiorette.gameroom.config;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class BotConfig {

  @Getter
  @NonNull
  private final String filename;

  @Getter
  private Properties properties;

  @SneakyThrows
  public void read() {
    final InputStream stream = getClass().getClassLoader().getResourceAsStream(filename);

    if (stream == null) {
      throw new FileNotFoundException(filename + "could not be found.");
    }

    this.properties = new Properties();
    properties.load(stream);
  }

  public String get(@NonNull Property prop) {
    return this.properties.getProperty(prop.getPath());
  }

  public String get(@NonNull Property prop, String def) {
    return this.properties.getProperty(prop.getPath(), def);
  }
}
