package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import io.reactivex.FlowableEmitter;
import io.reactivex.functions.Cancellable;

class FlowableChildEventListener implements ChildEventListener, TypeCancellable<Query> {
  private final FlowableEmitter<ChildEvent> emitter;

  FlowableChildEventListener(FlowableEmitter<ChildEvent> emitter) {
    this.emitter = emitter;
  }

  @Override
  public void onChildAdded(DataSnapshot snapshot, String previous) {
    emitter.onNext(ChildEvent.create(snapshot, ChildEvent.Type.CHILD_ADDED, previous));
  }

  @Override
  public void onChildChanged(DataSnapshot snapshot, String previous) {
    emitter.onNext(ChildEvent.create(snapshot, ChildEvent.Type.CHILD_CHANGED, previous));
  }

  @Override
  public void onChildRemoved(DataSnapshot snapshot) {
    emitter.onNext(ChildEvent.create(snapshot, ChildEvent.Type.CHILD_REMOVED));
  }

  @Override
  public void onChildMoved(DataSnapshot snapshot, String previous) {
    emitter.onNext(ChildEvent.create(snapshot, ChildEvent.Type.CHILD_MOVED, previous));
  }

  @Override
  public void onCancelled(DatabaseError error) {
    emitter.onError(error.toException());
  }

  @Override
  public Cancellable cancellable(Query query) {
    return new ChildEventCancellable(query, this);
  }
}
