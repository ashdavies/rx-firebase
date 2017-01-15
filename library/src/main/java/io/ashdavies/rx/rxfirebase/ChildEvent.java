package io.ashdavies.rx.rxfirebase;

import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;

@SuppressWarnings("WeakerAccess")
public final class ChildEvent {
  private final DataSnapshot snapshot;
  private final Type type;

  @Nullable
  private final String previous;

  private ChildEvent(DataSnapshot snapshot, Type type, @Nullable String previous) {
    this.snapshot = snapshot;
    this.type = type;

    this.previous = previous;
  }

  public static ChildEvent create(DataSnapshot dataSnapshot, Type type) {
    return new ChildEvent(dataSnapshot, type, null);
  }

  public static ChildEvent create(DataSnapshot dataSnapshot, Type type, String previous) {
    return new ChildEvent(dataSnapshot, type, previous);
  }

  public final DataSnapshot snapshot() {
    return snapshot;
  }

  public final Type type() {
    return type;
  }

  public final String previous() {
    return previous;
  }

  public enum Type {
    CHILD_ADDED,
    CHILD_CHANGED,
    CHILD_REMOVED,
    CHILD_MOVED
  }
}
