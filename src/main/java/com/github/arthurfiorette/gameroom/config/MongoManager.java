package com.github.arthurfiorette.gameroom.config;

import com.github.arthurfiorette.gameroom.Gameroom;
import com.github.arthurfiorette.gameroom.data.model.GuildConfig;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MongoManager {

  @Getter
  @NonNull
  private final Gameroom gameroom;

  @Getter
  private MongoClient client;

  @Getter
  private Datastore datastore;

  public void createClient() {
    final BotConfig config = gameroom.getConfig();

    MongoManager.log.info("Creating db client");
    final ConnectionString connection = new ConnectionString(config.get(Property.MONGO_CONNECTION));
    client = MongoClients.create(connection);
  }

  public void connect() {
    final BotConfig config = gameroom.getConfig();

    MongoManager.log.info("Connecting to database");
    datastore = Morphia.createDatastore(client, config.get(Property.MONGO_DATABASE));

    // Create the schema looking for classes in the same package of TempRoom
    datastore.getMapper().mapPackageFromClass(GuildConfig.class);
    datastore.ensureIndexes();
    datastore.ensureCaps();

    MongoManager.log.info("MongoDB connection is ready to use");
  }
}
