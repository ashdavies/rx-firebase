package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.FlowableEmitter;
import io.reactivex.functions.Cancellable;

class ValueEventFlowable implements ValueEventListener, TypeCancellable<Query> {

  private final FlowableEmitter<DataSnapshot> emitter;

  ValueEventFlowable(FlowableEmitter<DataSnapshot> emitter) {
    this.emitter = emitter;
  }

  @Override
  public void onDataChange(DataSnapshot snapshot) {
    emitter.onNext(snapshot);
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
