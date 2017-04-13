package io.ashdavies.rx.rxfirebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import io.reactivex.subscribers.TestSubscriber;

import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RxFirebaseDatabaseTest {

  private RxFirebaseDatabase rx;

  @Mock FirebaseDatabase database;
  @Mock DatabaseReference reference;

  @Mock Task<Void> task;

  @Before
  public void setUp() throws Exception {
    rx = RxFirebaseDatabase.getInstance(reference);
    given(reference.getDatabase()).willReturn(database);
  }

  @Test
  public void shouldSetPriority() throws Exception {
    given(reference.setPriority(null)).willReturn(task);

    rx.setPriority(null).subscribe();

    verify(reference).setPriority(null);
  }

  @Test
  public void shouldSetPriorityOnQueryReference() throws Exception {
    Query query = mock(Query.class);
    given(query.getRef()).willReturn(reference);
    given(reference.setPriority(null)).willReturn(task);

    RxFirebaseDatabase rx = RxFirebaseDatabase.with(query);
    rx.setPriority(null).subscribe();

    verify(reference).setPriority(null);
  }

  @Test
  public void shouldSetValueWithNullPriority() throws Exception {
    rx.setValue(null).subscribe();

    verify(reference).setValue(isNull(), isNull(), any(DatabaseReference.CompletionListener.class));
  }

  @Test
  public void shouldSetValue() throws Exception {
    rx.setValue("STRING").subscribe();

    verify(reference).setValue(Matchers.eq("STRING"), isNull(), any(DatabaseReference.CompletionListener.class));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void shouldUpdateChildren() throws Exception {
    Map<String, Object> map = mock(Map.class);
    rx.updateChildren(map).subscribe();

    verify(reference).updateChildren(map);
  }

  @Test
  public void shouldRemoveValue() throws Exception {
    rx.removeValue().subscribe();

    verify(reference).removeValue();
  }

  @Test
  public void shouldAddValueEventListener() throws Exception {
    rx.onValueEvent().subscribe();

    verify(reference).addValueEventListener(any(ValueEventListener.class));
  }

  @Test
  public void shouldAddSingleValueEventListener() throws Exception {
    rx.onSingleValueEvent().subscribe();

    verify(reference).addListenerForSingleValueEvent(any(ValueEventListener.class));
  }

  @Test
  public void shouldAddChildEventListener() throws Exception {
    rx.onChildEvent().subscribe();

    verify(reference).addChildEventListener(any(ChildEventListener.class));
  }

  @Test
  public void shouldBufferChildEvents() throws Exception {
    ArgumentCaptor<ChildEventListener> captor = forClass(ChildEventListener.class);
    TestSubscriber<ChildEvent> subscriber = rx.onChildEvent().test(1);
    verify(reference).addChildEventListener(captor.capture());

    ChildEventListener listener = captor.getValue();
    DataSnapshot snapshot = mock(DataSnapshot.class);

    listener.onChildAdded(snapshot, null);
    listener.onChildAdded(snapshot, null);

    subscriber
        .assertValueCount(1)
        .assertNoErrors();
  }

  @Test
  public void shouldFilterChildEvent() throws Exception {
    ArgumentCaptor<ChildEventListener> captor = forClass(ChildEventListener.class);
    TestSubscriber<ChildEvent> subscriber = rx.onChildEvent(ChildEvent.Type.CHILD_ADDED).test();
    verify(reference).addChildEventListener(captor.capture());

    ChildEventListener listener = captor.getValue();
    DataSnapshot snapshot = mock(DataSnapshot.class);

    listener.onChildAdded(snapshot, null);
    listener.onChildChanged(snapshot, null);

    subscriber
        .assertValueCount(1)
        .assertNoErrors();
  }

  @Test
  public void shouldParseChildEventValue() throws Exception {
    ArgumentCaptor<ChildEventListener> captor = forClass(ChildEventListener.class);
    TestSubscriber<String> subscriber = rx
        .onChildEventValue(ChildEvent.Type.CHILD_ADDED, String.class)
        .test();

    verify(reference).addChildEventListener(captor.capture());

    ChildEventListener listener = captor.getValue();
    DataSnapshot snapshot = mock(DataSnapshot.class);

    given(snapshot.getValue(String.class)).willReturn("BISCUITS");
    listener.onChildAdded(snapshot, null);

    subscriber
        .assertValue("BISCUITS")
        .assertNoErrors();
  }

  @Test
  public void shouldPurgeOutstandingWrites() throws Exception {
    rx.purgeOutstandingWrites();

    verify(database).purgeOutstandingWrites();
  }

  @Test
  public void shouldGoOnline() throws Exception {
    rx.goOnline();

    verify(database).goOnline();
  }

  @Test
  public void shouldGoOffline() throws Exception {
    rx.goOffline();

    verify(database).goOffline();
  }

  @Test
  public void shouldSetLogLevel() throws Exception {
    rx.setLogLevel(Logger.Level.DEBUG);

    verify(database).setLogLevel(Logger.Level.DEBUG);
  }
}
