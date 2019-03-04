package io.ashdavies.rx.rxfirebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.reactivex.ObservableEmitter

internal class ValueEventObservable(private val emitter: ObservableEmitter<DataSnapshot>) : ValueEventListener {

  override fun onDataChange(snapshot: DataSnapshot) = emitter.onNext(snapshot)

  override fun onCancelled(error: DatabaseError) = emitter.onError(error.toException())
}
