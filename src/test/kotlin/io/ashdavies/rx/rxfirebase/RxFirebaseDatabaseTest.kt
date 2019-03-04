package io.ashdavies.rx.rxfirebase

import com.google.android.gms.tasks.Tasks
import com.google.common.truth.Truth.assertThat
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Logger
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.nhaarman.mockito_kotlin.KArgumentCaptor
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import com.nhaarman.mockito_kotlin.verify
import io.ashdavies.rx.rxfirebase.ChildEvent.Type
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.jupiter.api.Test
import org.mockito.Answers.RETURNS_DEEP_STUBS

internal class RxFirebaseDatabaseTest {

  private val query: Query = mock(defaultAnswer = RETURNS_DEEP_STUBS)
  private val reference: DatabaseReference = mock()

  private val database = RxFirebaseDatabase(query)

  @Test
  fun `should get query reference child`() {
    given(query.ref.child("child")).willReturn(reference)

    assertThat(database.child("child")).isEqualTo(RxFirebaseDatabase(reference))
  }

  @Test
  fun `should order by child`() {
    given(query.orderByChild("child")).willReturn(reference)

    assertThat(database.orderByChild("child")).isEqualTo(RxFirebaseDatabase(reference))
  }

  @Test
  fun `should order by key`() {
    given(query.orderByKey()).willReturn(reference)

    assertThat(database.orderByKey()).isEqualTo(RxFirebaseDatabase(reference))
  }

  @Test
  fun `should order by priority`() {
    given(query.orderByPriority()).willReturn(reference)

    assertThat(database.orderByPriority()).isEqualTo(RxFirebaseDatabase(reference))
  }

  @Test
  fun `should order by value`() {
    given(query.orderByValue()).willReturn(reference)

    assertThat(database.orderByValue()).isEqualTo(RxFirebaseDatabase(reference))
  }

  @Test
  fun `should limit to first`() {
    given(query.limitToFirst(10)).willReturn(reference)

    assertThat(database.limitToFirst(10)).isEqualTo(RxFirebaseDatabase(reference))
  }

  @Test
  fun `should limit to last`() {
    given(query.limitToLast(10)).willReturn(reference)

    assertThat(database.limitToLast(10)).isEqualTo(RxFirebaseDatabase(reference))
  }

  @Test
  fun `should set priority`() {
    given(query.ref.setPriority("HIGH")).willReturn(Tasks.forResult(null))

    database
        .setPriority("HIGH")
        .test()
        .assertResult()
  }

  @Test
  fun `should set value`() {
    given(query.ref.setValue("VALUE", "HIGH")).willReturn(Tasks.forResult(null))

    database
        .setValue("VALUE", "HIGH")
        .test()
        .assertResult()
  }

  @Test
  fun `should update children`() {
    given(query.ref.updateChildren(mapOf("key" to "value"))).willReturn(Tasks.forResult(null))

    database
        .updateChildren(mapOf("key" to "value"))
        .test()
        .assertResult()
  }

  @Test
  fun `should remove value`() {
    given(query.ref.removeValue()).willReturn(Tasks.forResult(null))

    database
        .removeValue()
        .test()
        .assertResult()
  }

  @Test
  fun `should emit on value event`() {
    val captor: KArgumentCaptor<ValueEventListener> = argumentCaptor()
    val snapshot: DataSnapshot = mock()

    verify(query).addValueEventListener(captor.capture())

    val subscriber: TestSubscriber<DataSnapshot> = database
        .onValueEvent()
        .test()

    captor
        .lastValue
        .onDataChange(snapshot)

    subscriber.assertValue(snapshot)
  }

  @Test
  fun `should emit single value event`() {
    val captor: KArgumentCaptor<ValueEventListener> = argumentCaptor()
    val snapshot: DataSnapshot = mock()

    verify(query).addListenerForSingleValueEvent(captor.capture())

    val observer: TestObserver<DataSnapshot> = database
        .onSingleValueEvent()
        .test()

    captor
        .lastValue
        .onDataChange(snapshot)

    observer.assertValue(snapshot)
  }

  @Test
  fun `should emit on child event`() {
    val captor: KArgumentCaptor<ChildEventListener> = argumentCaptor()
    val snapshot: DataSnapshot = mock()

    verify(query).addChildEventListener(captor.capture())

    val subscriber: TestSubscriber<ChildEvent> = database
        .onChildEvent()
        .test()

    captor
        .lastValue
        .onChildAdded(snapshot, null)

    subscriber.assertValue(ChildEvent(snapshot, Type.CHILD_ADDED))
  }

  @Test
  fun `should filter child event`() {
    val captor: KArgumentCaptor<ChildEventListener> = argumentCaptor()
    val snapshot: DataSnapshot = mock()

    verify(query).addChildEventListener(captor.capture())

    val subscriber: TestSubscriber<ChildEvent> = database
        .onChildEvent(Type.CHILD_ADDED)
        .test()

    val listener: ChildEventListener = captor.lastValue
    listener.onChildAdded(snapshot, null)
    listener.onChildMoved(snapshot, null)

    subscriber.assertValue(ChildEvent(snapshot, Type.CHILD_ADDED))
  }

  @Test
  fun `should emit child event value`() {
    val captor: KArgumentCaptor<ChildEventListener> = argumentCaptor()
    val snapshot: DataSnapshot = mock()

    verify(query).addChildEventListener(captor.capture())

    val subscriber: TestSubscriber<String> = database
        .onChildEventValue(String::class.java)
        .test()

    given(snapshot.getValue(String::class.java)).willReturn("Hello World")

    captor
        .lastValue
        .onChildAdded(snapshot, null)

    subscriber.assertValue("Hello World")
  }

  @Test
  fun `should filter child event value`() {
    val captor: KArgumentCaptor<ChildEventListener> = argumentCaptor()
    val snapshot: DataSnapshot = mock()

    verify(query).addChildEventListener(captor.capture())

    val subscriber: TestSubscriber<String> = database
        .onChildEventValue(Type.CHILD_ADDED, String::class.java)
        .test()

    given(snapshot.getValue(String::class.java)).willReturn("Hello World", "Hello Universe")

    val listener: ChildEventListener = captor.lastValue
    listener.onChildAdded(snapshot, null)
    listener.onChildMoved(snapshot, null)

    subscriber.assertValue("Hello World")
  }

  @Test
  fun `should purge outstanding writes`() {
    database.purgeOutstandingWrites()

    then(query.ref.database)
        .should()
        .purgeOutstandingWrites()
  }

  @Test
  fun `should go online`() {
    database.goOnline()

    then(query.ref.database)
        .should()
        .goOnline()
  }

  @Test
  fun `should go offline`() {
    database.goOffline()

    then(query.ref.database)
        .should()
        .goOffline()
  }

  @Test
  fun `should set log level`() {
    database.setLogLevel(Logger.Level.DEBUG)

    then(query.ref.database)
        .should()
        .setLogLevel(Logger.Level.DEBUG)
  }
}
