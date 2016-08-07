package io.ashdavies.rxfirebase;

import com.firebase.client.DataSnapshot;
import com.firebase.client.core.view.Event;

public final class ChildEvent {
  private final DataSnapshot dataSnapshot;
  private final Event.EventType eventType;
  private final String previousChildName;

  private ChildEvent(final DataSnapshot dataSnapshot, final Event.EventType eventType, final String previousChildName) {
    this.dataSnapshot = dataSnapshot;
    this.eventType = eventType;
    this.previousChildName = previousChildName;
  }

  public static ChildEvent create(final DataSnapshot dataSnapshot, final Event.EventType eventType) {
    return new ChildEvent(dataSnapshot, eventType, null);
  }

  public static ChildEvent create(final DataSnapshot dataSnapshot, final Event.EventType eventType, final String previousChildName) {
    return new ChildEvent(dataSnapshot, eventType, previousChildName);
  }

  public final  DataSnapshot getDataSnapshot() {
    return dataSnapshot;
  }

  public final Event.EventType getEventType() {
    return eventType;
  }

  public final String getPreviousChildName() {
    return previousChildName;
  }
}
