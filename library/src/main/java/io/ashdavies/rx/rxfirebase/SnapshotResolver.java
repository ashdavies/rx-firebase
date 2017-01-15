package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.DataSnapshot;

interface SnapshotResolver<T> {

  T resolve(DataSnapshot snapshot);
}
