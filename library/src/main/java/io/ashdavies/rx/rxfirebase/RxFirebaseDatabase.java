package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;

import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;

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

  public <T> Flowable<T> getReference(SnapshotResolver<T> resolver) {
    return flowable(database.getReference(), resolver);
  }

  public <T> Flowable<T> getReference(String string, SnapshotResolver<T> resolver) {
    return flowable(database.getReference(string), resolver);
  }

  public <T> Flowable<T> getReferenceFromUrl(String string, SnapshotResolver<T> resolver) {
    return flowable(database.getReferenceFromUrl(string), resolver);
  }

  private <T> Flowable<T> flowable(Query query, final SnapshotResolver<T> resolver) {
    return Flowable.create(new ChildEventOnSubscribe(query), FlowableEmitter.BackpressureMode.BUFFER)
        .map(new ChildEventResolver<>(resolver));
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
