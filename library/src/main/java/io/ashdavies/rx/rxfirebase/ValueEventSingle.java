package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.SingleEmitter;
import io.reactivex.functions.Cancellable;

class ValueEventSingle<T> implements ValueEventListener, TypeCancellable<Query> {

  private final SingleEmitter<T> emitter;
  private final SnapshotResolver<T> resolver;

  ValueEventSingle(SingleEmitter<T> emitter, SnapshotResolver<T> resolver) {
    this.emitter = emitter;
    this.resolver = resolver;
  }

  @Override
  public void onDataChange(DataSnapshot snapshot) {
    emitter.onSuccess(resolver.resolve(snapshot));
  }

  @Override
  public void onCancelled(DatabaseError error) {
    emitter.onError(error.toException());
  }

  @Override
  public Cancellable cancellable(Query query) {
    return new ValueEventCancellable(query, this);
  }
}
