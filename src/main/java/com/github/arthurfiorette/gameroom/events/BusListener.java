package com.github.arthurfiorette.gameroom.events;

import com.google.common.eventbus.Subscribe;

/**
 * Simple interface to standartize all events.
 * <p>
 * <b>Mark with {@link Subscribe} to use it </b>
 */
public interface BusListener {

  default void onRoomRequest(RoomRequestEvent event) {}

}
