package io.ashdavies.rx.rxfirebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.nhaarman.mockito_kotlin.isA
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import io.reactivex.SingleEmitter
import org.junit.jupiter.api.Test

internal class ValueEventSingleTest {

  private val emitter: SingleEmitter<DataSnapshot> = mock()
  private val snapshot: DataSnapshot = mock()

  private val listener = ValueEventSingle(emitter)

  @Test
  fun `should emit snapshot`() {
    listener.onDataChange(snapshot)

    then(emitter)
        .should()
        .onSuccess(snapshot)
  }

  @Test
  fun `should emit exception`() {
    listener.onCancelled(DatabaseError.fromCode(418))

    then(emitter)
        .should()
        .onError(isA<DatabaseException>())
  }

}
