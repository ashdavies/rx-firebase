package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Single;

@SuppressWarnings("WeakerAccess")
public class RxFirebaseDatabase {
  private final FirebaseDatabase database;

  private RxFirebaseDatabase(FirebaseDatabase database) {
    this.database = database;
  }

  public static RxFirebaseDatabase getInstance() {
    return getInstance(FirebaseDatabase.getInstance());
  }

  public static RxFirebaseDatabase getInstance(FirebaseDatabase database) {
    return new RxFirebaseDatabase(database);
  }

  public Completable setValue(Object value) {
    return setValue(value, null);
  }

  public Completable setValue(Object value, Object priority) {
    return Completable.create(new ValueOnSubscribe(database.getReference(), value, priority));
  }

  public <T> Flowable<T> onValueEvent(String path, SnapshotResolver<T> resolver) {
    return Flowable.create(new ValueEventOnSubscribe<>(database.getReference(path), resolver), FlowableEmitter.BackpressureMode.BUFFER);
  }

  public <T> Single<T> onSingleValueEvent(String path, SnapshotResolver<T> resolver) {
    return Single.create(new SingleValueEventOnSubscribe<>(database.getReference(path), resolver));
  }

  public Flowable<ChildEvent> onChildEvent(String path) {
    return Flowable.create(new ChildEventOnSubscribe(database.getReference(path)), FlowableEmitter.BackpressureMode.BUFFER);
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

  public RxFirebaseDatabase purgeOutstandingWrites() {
    database.purgeOutstandingWrites();
    return this;
  }

  public RxFirebaseDatabase goOnline() {
    database.goOnline();
    return this;
  }

  public RxFirebaseDatabase goOffline() {
    database.goOffline();
    return this;
  }

  public RxFirebaseDatabase setLogLevel(Logger.Level level) {
    database.setLogLevel(level);
    return this;
  }

  public RxFirebaseDatabase setPersistenceEnabled(boolean enabled) {
    database.setPersistenceEnabled(enabled);
    return this;
  }
}
