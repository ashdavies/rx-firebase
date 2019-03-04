package io.ashdavies.rx.rxfirebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.reactivex.SingleEmitter

internal class ValueEventSingle(private val emitter: SingleEmitter<DataSnapshot>) : ValueEventListener {

  override fun onDataChange(snapshot: DataSnapshot) = emitter.onSuccess(snapshot)

  override fun onCancelled(error: DatabaseError) = emitter.onError(error.toException())
}
