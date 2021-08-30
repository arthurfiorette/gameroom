package com.github.arthurfiorette.gameroom.shard;

import java.util.Collection;
import java.util.function.IntFunction;

import com.google.common.collect.ImmutableList;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ShardingUtils {

  /**
   * Generate a int function list for every shard id needed.
   */
  public Collection<IntFunction<Object>> getJdaListeners(
    final IntFunction<ShardInstance> generator
  ) {
    return ImmutableList.of(
      // shard instance itself
      generator::apply,
      id -> generator.apply(id).getReadyListener()
    );
  }
}
