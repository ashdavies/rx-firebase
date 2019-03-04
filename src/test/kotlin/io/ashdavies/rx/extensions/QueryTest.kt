package io.ashdavies.rx.extensions

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.nhaarman.mockito_kotlin.KArgumentCaptor
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import com.nhaarman.mockito_kotlin.verify
import io.ashdavies.rx.rxfirebase.ChildEvent
import io.ashdavies.rx.rxfirebase.ChildEvent.Type
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class QueryTest {

  private val snapshot: DataSnapshot = mock()
  private val query: Query = mock()

  private val child: KArgumentCaptor<ChildEventListener> = argumentCaptor()
  private val value: KArgumentCaptor<ValueEventListener> = argumentCaptor()

  @BeforeEach
  fun setUp() {
    verify(query).addChildEventListener(child.capture())
    verify(query).addValueEventListener(value.capture())
  }

  @Test
  fun `should emit child event`() {
    val observer: TestObserver<ChildEvent> = query
        .onChildEvent()
        .test()

    child
        .lastValue
        .onChildAdded(snapshot, null)

    observer.assertValue(ChildEvent(snapshot, Type.CHILD_ADDED))
  }

  @Test
  fun `should remove event listener on child event dispose`() {
    query
        .onChildEvent()
        .test()
        .dispose()

    then(query)
        .should()
        .removeEventListener(child.lastValue)
  }

  @Test
  fun `should emit child event value`() {
    given(snapshot.getValue(String::class.java)).willReturn("Hello World")

    val observer: TestObserver<String> = query
        .onChildEventValue<String>()
        .test()

    child
        .lastValue
        .onChildAdded(snapshot, null)

    observer.assertValue("Hello World")
  }

  @Test
  fun `should emit single value event`() {
    val observer: TestObserver<DataSnapshot> = query
        .onSingleValueEvent()
        .test()

    value
        .lastValue
        .onDataChange(snapshot)

    observer.assertValue(snapshot)
  }

  @Test
  fun `should remove event listener on single value event dispose`() {
    query
        .onSingleValueEvent()
        .test()
        .dispose()

    then(query)
        .should()
        .removeEventListener(value.lastValue)
  }

  @Test
  fun `should emit value event`() {
    val observer: TestObserver<DataSnapshot> = query
        .onValueEvent()
        .test()

    value
        .lastValue
        .onDataChange(snapshot)

    observer.assertValue(snapshot)
  }

  @Test
  fun `should remove event listener on value event dispose`() {
    query
        .onValueEvent()
        .test()
        .dispose()

    then(query)
        .should()
        .removeEventListener(value.lastValue)
  }
}
