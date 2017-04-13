package io.ashdavies.rx.rxfirebase;

import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;

import java.util.Map;

import io.ashdavies.rx.rxtasks.RxTasks;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@SuppressWarnings("WeakerAccess")
public final class RxFirebaseDatabase {

  private final DatabaseReference reference;

  private RxFirebaseDatabase(DatabaseReference reference) {
    this.reference = reference;
  }

  public static RxFirebaseDatabase getInstance() {
    return getInstance(FirebaseDatabase.getInstance().getReference());
  }

  public static RxFirebaseDatabase getInstance(String path, Object... args) {
    return getInstance(FirebaseDatabase.getInstance().getReference(String.format(path, args)));
  }

  public static RxFirebaseDatabase getInstance(DatabaseReference reference) {
    return new RxFirebaseDatabase(reference);
  }

  public static RxFirebaseDatabase with(Query query) {
    return getInstance(query.getRef());
  }

  public RxFirebaseDatabase child(String child) {
    return getInstance(reference.child(child));
  }

  @CheckResult
  public Completable setPriority(Object object) {
    return RxTasks.completable(reference.setPriority(object));
  }

  @CheckResult
  public Completable setValue(Object value) {
    return setValue(value, null);
  }

  @CheckResult
  public Completable setValue(Object value, @Nullable Object priority) {
    return Completable.create(new SetValueOnSubscribe(reference, value, priority));
  }

  @CheckResult
  public Completable updateChildren(Map<String, Object> map) {
    return RxTasks.completable(reference.updateChildren(map));
  }

  @CheckResult
  public Completable removeValue() {
    return RxTasks.completable(reference.removeValue());
  }

  @CheckResult
  public Flowable<DataSnapshot> onValueEvent() {
    return Flowable.create(new ValueEventOnSubscribe(reference), BackpressureStrategy.BUFFER);
  }

  @CheckResult
  public Single<DataSnapshot> onSingleValueEvent() {
    return Single.create(new SingleValueEventOnSubscribe(reference));
  }

  @CheckResult
  public Flowable<ChildEvent> onChildEvent() {
    return Flowable.create(new ChildEventOnSubscribe(reference), BackpressureStrategy.BUFFER);
  }

  @CheckResult
  public Flowable<ChildEvent> onChildEvent(ChildEvent.Type childEventType) {
    return onChildEvent().filter(new ChildEventTypePredicate(childEventType));
  }

  @CheckResult
  public <T> Flowable<T> onChildEventValue(ChildEvent.Type childEventType, Class<T> kls) {
    return onChildEvent(childEventType).compose(new ChildEventClassTransformer<>(kls));
  }

  public RxFirebaseDatabase purgeOutstandingWrites() {
    reference.getDatabase().purgeOutstandingWrites();
    return this;
  }

  public RxFirebaseDatabase goOnline() {
    reference.getDatabase().goOnline();
    return this;
  }

  public RxFirebaseDatabase goOffline() {
    reference.getDatabase().goOffline();
    return this;
  }

  public RxFirebaseDatabase setLogLevel(Logger.Level level) {
    reference.getDatabase().setLogLevel(level);
    return this;
  }
}
