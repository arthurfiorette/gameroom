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

  public String get(@NonNull final Property prop) {
    return properties.getProperty(prop.getPath());
  }

  public String get(@NonNull final Property prop, final String def) {
    return properties.getProperty(prop.getPath(), def);
  }

  public boolean has(@NonNull final Property prop) {
    return properties.contains(prop.getPath());
  }

  public int getInt(@NonNull final Property prop) {
    return Integer.parseInt(get(prop));
  }

  public int getInt(@NonNull final Property prop, final int def) {
    return Integer.parseInt(get(prop, def + ""));
  }

  public boolean getBoolean(@NonNull final Property prop) {
    return Boolean.parseBoolean(get(prop));
  }
}
