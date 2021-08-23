package com.github.arthurfiorette.gameroom.shard;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@Builder
public class ShardOptions {

  @Getter
  @Singular
  private List<Object> shardListeners;

  @Getter
  @Singular
  private List<Object> jdaListeners;
}
