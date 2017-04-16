package io.ashdavies.rx.rxfirebase;

import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import io.ashdavies.rx.rxtasks.RxTasks;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public final class RxFirebaseDatabase {

  private final Query query;

  private RxFirebaseDatabase(Query query) {
    this.query = query;
  }

  public static RxFirebaseDatabase getInstance() {
    return getInstance(FirebaseDatabase.getInstance().getReference());
  }

  public static RxFirebaseDatabase getInstance(String path, Object... args) {
    return getInstance(FirebaseDatabase.getInstance().getReference(String.format(path, args)));
  }

  public static RxFirebaseDatabase getInstance(Query query) {
    return new RxFirebaseDatabase(query);
  }

  @CheckResult
  public RxFirebaseDatabase child(String child) {
    return getInstance(query.getRef().child(child));
  }

  @CheckResult
  public RxFirebaseDatabase orderByChild(String child) {
    return getInstance(query.orderByChild(child));
  }

  @CheckResult
  public RxFirebaseDatabase orderByKey() {
    return getInstance(query.orderByKey());
  }

  @CheckResult
  public RxFirebaseDatabase orderByPriority() {
    return getInstance(query.orderByPriority());
  }

  @CheckResult
  public RxFirebaseDatabase orderByValue() {
    return getInstance(query.orderByValue());
  }

  @CheckResult
  public RxFirebaseDatabase limitToFirst(int i) {
    return getInstance(query.limitToFirst(i));
  }

  @CheckResult
  public RxFirebaseDatabase limitToLast(int i) {
    return getInstance(query.limitToLast(i));
  }

  @CheckResult
  public Completable setPriority(Object object) {
    return RxTasks.completable(query.getRef().setPriority(object));
  }

  @CheckResult
  public Completable setValue(Object value) {
    return setValue(value, null);
  }

  @CheckResult
  public Completable setValue(Object value, @Nullable Object priority) {
    return Completable.create(new SetValueOnSubscribe(query.getRef(), value, priority));
  }

  @CheckResult
  public Completable updateChildren(Map<String, Object> map) {
    return RxTasks.completable(query.getRef().updateChildren(map));
  }

  @CheckResult
  public Completable removeValue() {
    return RxTasks.completable(query.getRef().removeValue());
  }

  @CheckResult
  public Flowable<DataSnapshot> onValueEvent() {
    return Flowable.create(new ValueEventOnSubscribe(query), BackpressureStrategy.BUFFER);
  }

  @CheckResult
  public Single<DataSnapshot> onSingleValueEvent() {
    return Single.create(new SingleValueEventOnSubscribe(query));
  }

  @CheckResult
  public Flowable<ChildEvent> onChildEvent() {
    return Flowable.create(new ChildEventOnSubscribe(query), BackpressureStrategy.BUFFER);
  }

  @CheckResult
  public Flowable<ChildEvent> onChildEvent(ChildEvent.Type childEventType) {
    return onChildEvent().filter(new ChildEventTypePredicate(childEventType));
  }

  @CheckResult()
  public <T> Flowable<T> onChildEventValue(ChildEvent.Type childEventType, Class<T> kls) {
    return onChildEvent(childEventType).compose(new ChildEventClassTransformer<>(kls));
  }

  @SuppressWarnings("UnusedReturnValue")
  public RxFirebaseDatabase purgeOutstandingWrites() {
    query.getRef().getDatabase().purgeOutstandingWrites();
    return this;
  }

  @SuppressWarnings("UnusedReturnValue")
  public RxFirebaseDatabase goOnline() {
    query.getRef().getDatabase().goOnline();
    return this;
  }

  @SuppressWarnings("UnusedReturnValue")
  public RxFirebaseDatabase goOffline() {
    query.getRef().getDatabase().goOffline();
    return this;
  }

  @SuppressWarnings("UnusedReturnValue")
  public RxFirebaseDatabase setLogLevel(Logger.Level level) {
    query.getRef().getDatabase().setLogLevel(level);
    return this;
  }
}
