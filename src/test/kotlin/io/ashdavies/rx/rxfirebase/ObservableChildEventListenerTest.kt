package io.ashdavies.rx.rxfirebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.nhaarman.mockito_kotlin.isA
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import io.ashdavies.rx.rxfirebase.ChildEvent.Type
import io.reactivex.ObservableEmitter
import org.junit.jupiter.api.Test

internal class ObservableChildEventListenerTest {

  private val emitter: ObservableEmitter<ChildEvent> = mock()
  private val snapshot: DataSnapshot = mock()

  private val listener = ObservableChildEventListener(emitter)

  @Test
  fun `should emit on child added event`() {
    listener.onChildAdded(snapshot, null)

    then(emitter)
        .should()
        .onNext(ChildEvent(snapshot, Type.CHILD_ADDED))
  }

  @Test
  fun `should emit on child changed events`() {
    listener.onChildChanged(snapshot, null)

    then(emitter)
        .should()
        .onNext(ChildEvent(snapshot, Type.CHILD_CHANGED))
  }

  @Test
  fun `should emit on child removed event`() {
    listener.onChildRemoved(snapshot)

    then(emitter)
        .should()
        .onNext(ChildEvent(snapshot, Type.CHILD_REMOVED))
  }

  @Test
  fun `should emit on child moved event`() {
    listener.onChildMoved(snapshot, null)

    then(emitter)
        .should()
        .onNext(ChildEvent(snapshot, Type.CHILD_MOVED))
  }

  @Test
  fun `should emit error on cancellation`() {
    listener.onCancelled(DatabaseError.fromCode(418))

    then(emitter)
        .should()
        .onError(isA<DatabaseException>())
  }
}
