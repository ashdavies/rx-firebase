package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Single;

@SuppressWarnings("WeakerAccess")
public final class RxFirebaseDatabaseReference {

  private final DatabaseReference reference;

  public RxFirebaseDatabaseReference(DatabaseReference reference) {
    this.reference = reference;
  }

  public Completable setValue(Object value) {
    return setValue(value, null);
  }

  public Completable setValue(Object value, Object priority) {
    return Completable.create(new ValueOnSubscribe(reference, value, priority));
  }

  public Flowable<DataSnapshot> onValueEvent(String path, SnapshotResolver<DataSnapshot> resolver) {
    return Flowable.create(new ValueEventOnSubscribe<>(reference.child(path), resolver), FlowableEmitter.BackpressureMode.BUFFER);
  }

  public Single<DataSnapshot> onSingleValueEvent(String path, SnapshotResolver<DataSnapshot> resolver) {
    return Single.create(new SingleValueEventOnSubscribe<>(reference.child(path), resolver));
  }

  public Flowable<ChildEvent> onChildEvent(String path) {
    return Flowable.create(new ChildEventOnSubscribe(reference.child(path)), FlowableEmitter.BackpressureMode.BUFFER);
  }

  public final Flowable<ChildEvent> onChildAdded(String path) {
    return onChildEvent(path).filter(new ChildEventTypePredicate(ChildEvent.Type.CHILD_ADDED));
  }

  public final Flowable<ChildEvent> onChildChanged(String path) {
    return onChildEvent(path).filter(new ChildEventTypePredicate(ChildEvent.Type.CHILD_CHANGED));
  }

  public final Flowable<ChildEvent> onChildRemoved(String path) {
    return onChildEvent(path).filter(new ChildEventTypePredicate(ChildEvent.Type.CHILD_REMOVED));
  }

  public final Flowable<ChildEvent> onChildMoved(String path) {
    return onChildEvent(path).filter(new ChildEventTypePredicate(ChildEvent.Type.CHILD_MOVED));
  }
}
