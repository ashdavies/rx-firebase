package io.ashdavies.rx.rxfirebase

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import io.ashdavies.rx.rxfirebase.ChildEvent.Type
import io.reactivex.ObservableEmitter

internal class ObservableChildEventListener(private val emitter: ObservableEmitter<ChildEvent>) : ChildEventListener {

  override fun onChildAdded(snapshot: DataSnapshot, previous: String?) = emitter.onNext(ChildEvent(snapshot, Type.CHILD_ADDED, previous))

  override fun onChildChanged(snapshot: DataSnapshot, previous: String?) = emitter.onNext(ChildEvent(snapshot, ChildEvent.Type.CHILD_CHANGED, previous))

  override fun onChildRemoved(snapshot: DataSnapshot) = emitter.onNext(ChildEvent(snapshot, ChildEvent.Type.CHILD_REMOVED))

  override fun onChildMoved(snapshot: DataSnapshot, previous: String?) = emitter.onNext(ChildEvent(snapshot, ChildEvent.Type.CHILD_MOVED, previous))

  override fun onCancelled(error: DatabaseError) = emitter.onError(error.toException())
}
