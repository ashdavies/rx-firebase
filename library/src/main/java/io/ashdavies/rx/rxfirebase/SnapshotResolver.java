package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.DataSnapshot;

@SuppressWarnings("WeakerAccess")
public interface SnapshotResolver<T> {

  T resolve(DataSnapshot snapshot);
}
