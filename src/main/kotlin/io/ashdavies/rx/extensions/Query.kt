package io.ashdavies.rx.extensions

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Query
import io.ashdavies.rx.rxfirebase.ChildEvent
import io.ashdavies.rx.rxfirebase.ChildEvent.Type
import io.ashdavies.rx.rxfirebase.ChildEventMapper
import io.ashdavies.rx.rxfirebase.ObservableChildEventListener
import io.ashdavies.rx.rxfirebase.ValueEventObservable
import io.ashdavies.rx.rxfirebase.ValueEventSingle
import io.reactivex.Observable
import io.reactivex.Single

fun Query.onChildEvent(): Observable<ChildEvent> = Observable.create {
  val listener = ObservableChildEventListener(it)
  it.setCancellable { removeEventListener(listener) }
  addChildEventListener(listener)
}

fun Query.onChildEvent(type: ChildEvent.Type): Observable<ChildEvent> = onChildEvent().filter { it.type == type }

inline fun <reified T> Query.onChildEventValue(): Observable<T> = onChildEventValue(T::class.java)

inline fun <reified T> Query.onChildAdded(): Observable<T> = onChildEventValue(Type.CHILD_ADDED, T::class.java)

inline fun <reified T> Query.onChildChanged(): Observable<T> = onChildEventValue(Type.CHILD_CHANGED, T::class.java)

inline fun <reified T> Query.onChildRemoved(): Observable<T> = onChildEventValue(Type.CHILD_REMOVED, T::class.java)

inline fun <reified T> Query.onChildMoved(): Observable<T> = onChildEventValue(Type.CHILD_MOVED, T::class.java)

fun <T> Query.onChildEventValue(kls: Class<T>): Observable<T> = onChildEvent().flatMapMaybe(ChildEventMapper(kls))

fun <T> Query.onChildEventValue(type: ChildEvent.Type, kls: Class<T>): Observable<T> = onChildEvent()
    .filter { it.type == type }
    .flatMapMaybe(ChildEventMapper(kls))

fun Query.onSingleValueEvent(): Single<DataSnapshot> = Single.create {
  val listener = ValueEventSingle(it)
  it.setCancellable { removeEventListener(listener) }
  addListenerForSingleValueEvent(listener)
}

fun Query.onValueEvent(): Observable<DataSnapshot> = Observable.create {
  val listener = ValueEventObservable(it)
  it.setCancellable { removeEventListener(listener) }
  addValueEventListener(listener)
}
