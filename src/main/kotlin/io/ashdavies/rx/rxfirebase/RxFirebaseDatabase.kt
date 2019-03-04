package io.ashdavies.rx.rxfirebase

import androidx.annotation.CheckResult
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger
import com.google.firebase.database.Query
import io.ashdavies.rx.extensions.onChildEvent
import io.ashdavies.rx.extensions.onSingleValueEvent
import io.ashdavies.rx.extensions.onValueEvent
import io.ashdavies.rx.rxtasks.toCompletable
import io.reactivex.BackpressureStrategy
import io.reactivex.BackpressureStrategy.BUFFER
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.lang.String.format

@Deprecated("Use extension functions and/or RxTasks instead")
data class RxFirebaseDatabase(private val query: Query) {

  @CheckResult
  fun child(child: String): RxFirebaseDatabase = RxFirebaseDatabase(query.ref.child(child))

  @CheckResult
  fun orderByChild(child: String): RxFirebaseDatabase = RxFirebaseDatabase(query.orderByChild(child))

  @CheckResult
  fun orderByKey(): RxFirebaseDatabase = RxFirebaseDatabase(query.orderByKey())

  @CheckResult
  fun orderByPriority(): RxFirebaseDatabase = RxFirebaseDatabase(query.orderByPriority())

  @CheckResult
  fun orderByValue(): RxFirebaseDatabase = RxFirebaseDatabase(query.orderByValue())

  @CheckResult
  fun limitToFirst(limit: Int): RxFirebaseDatabase = RxFirebaseDatabase(query.limitToFirst(limit))

  @CheckResult
  fun limitToLast(limit: Int): RxFirebaseDatabase = RxFirebaseDatabase(query.limitToLast(limit))

  @CheckResult
  fun setPriority(priority: Any): Completable = query
      .ref
      .setPriority(priority)
      .toCompletable()

  @CheckResult
  @JvmOverloads
  fun setValue(value: Any, priority: Any? = null): Completable = query
      .ref
      .setValue(value, priority)
      .toCompletable()

  @CheckResult
  fun updateChildren(map: Map<String, Any>): Completable = query
      .ref
      .updateChildren(map)
      .toCompletable()

  @CheckResult
  fun removeValue(): Completable = query
      .ref
      .removeValue()
      .toCompletable()

  @CheckResult
  @JvmOverloads
  fun onValueEvent(strategy: BackpressureStrategy = BUFFER): Flowable<DataSnapshot> = query
      .onValueEvent()
      .toFlowable(strategy)

  @CheckResult
  fun onSingleValueEvent(): Single<DataSnapshot> = query.onSingleValueEvent()

  @CheckResult
  @JvmOverloads
  fun onChildEvent(strategy: BackpressureStrategy = BUFFER): Flowable<ChildEvent> = query
      .onChildEvent()
      .toFlowable(strategy)

  @CheckResult
  @JvmOverloads
  fun onChildEvent(type: ChildEvent.Type, strategy: BackpressureStrategy = BUFFER): Flowable<ChildEvent> = query
      .onChildEvent(type)
      .toFlowable(strategy)

  @CheckResult
  @JvmOverloads
  fun <T> onChildEventValue(kls: Class<T>, strategy: BackpressureStrategy = BUFFER): Flowable<T> {
    return onChildEvent(strategy).flatMapMaybe(ChildEventMapper(kls))
  }

  @CheckResult
  @JvmOverloads
  fun <T> onChildEventValue(type: ChildEvent.Type, kls: Class<T>, strategy: BackpressureStrategy = BUFFER): Flowable<T> {
    return onChildEvent(type, strategy).flatMapMaybe(ChildEventMapper(kls))
  }

  fun purgeOutstandingWrites(): RxFirebaseDatabase = apply {
    query
        .ref
        .database
        .purgeOutstandingWrites()
  }

  fun goOnline(): RxFirebaseDatabase = apply {
    query
        .ref
        .database
        .goOnline()
  }

  fun goOffline(): RxFirebaseDatabase = apply {
    query
        .ref
        .database
        .goOffline()
  }

  fun setLogLevel(level: Logger.Level): RxFirebaseDatabase = apply {
    query
        .ref
        .database
        .setLogLevel(level)
  }

  companion object {

    private val reference: DatabaseReference
      get() = FirebaseDatabase
          .getInstance()
          .reference

    private fun getReference(path: String, vararg args: Any): DatabaseReference = FirebaseDatabase
        .getInstance()
        .getReference(format(path, *args))

    @JvmStatic
    fun getInstance() = RxFirebaseDatabase(reference)

    @JvmStatic
    fun getInstance(path: String, vararg args: Any) = RxFirebaseDatabase(getReference(format(path, *args)))
  }
}
