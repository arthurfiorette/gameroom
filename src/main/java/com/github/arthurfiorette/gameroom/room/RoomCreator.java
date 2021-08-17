package com.github.arthurfiorette.gameroom.room;

import com.github.arthurfiorette.gameroom.Gameroom;
import com.github.arthurfiorette.gameroom.events.BusListener;
import com.github.arthurfiorette.gameroom.events.RoomRequestEvent;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoomCreator implements BusListener {

  @Getter
  @NonNull
  private final Gameroom gameroom;

  @Override
  @Subscribe
  @AllowConcurrentEvents
  public void onRoomRequest(RoomRequestEvent event) {}
}
