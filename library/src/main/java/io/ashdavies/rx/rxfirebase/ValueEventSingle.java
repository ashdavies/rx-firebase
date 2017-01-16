package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.SingleEmitter;
import io.reactivex.functions.Cancellable;

class ValueEventSingle implements ValueEventListener, TypeCancellable<Query> {

  private final SingleEmitter<DataSnapshot> emitter;

  ValueEventSingle(SingleEmitter<DataSnapshot> emitter) {
    this.emitter = emitter;
  }

  @Override
  public void onDataChange(DataSnapshot snapshot) {
    emitter.onSuccess(snapshot);
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
