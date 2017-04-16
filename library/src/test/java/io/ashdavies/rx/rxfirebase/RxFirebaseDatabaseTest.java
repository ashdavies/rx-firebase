package io.ashdavies.rx.rxfirebase;

import android.annotation.SuppressLint;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import io.reactivex.subscribers.TestSubscriber;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class RxFirebaseDatabaseTest {

  private RxFirebaseDatabase rx;

  @Mock FirebaseDatabase database;
  @Mock DatabaseReference reference;
  @Mock Query query;

  @Mock Task<Void> task;

  @Before
  public void setUp() throws Exception {
    given(query.getRef()).willReturn(reference);
    given(reference.getDatabase()).willReturn(database);

    rx = RxFirebaseDatabase.getInstance(query);
  }

  @Test
  @SuppressLint("CheckResult")
  public void shouldGetReferenceChild() throws Exception {
    rx.child("child");

    then(reference).should().child("child");
  }

  @Test
  @SuppressLint("CheckResult")
  public void shouldOrderByChild() throws Exception {
    rx.orderByChild("child");

    then(query).should().orderByChild("child");
  }

  @Test
  @SuppressLint("CheckResult")
  public void shouldOrderByKey() throws Exception {
    rx.orderByKey();

    then(query).should().orderByKey();
  }

  @Test
  @SuppressLint("CheckResult")
  public void shouldOrderByPriority() throws Exception {
    rx.orderByPriority();

    then(query).should().orderByPriority();
  }

  @Test
  @SuppressLint("CheckResult")
  public void shouldOrderByValue() throws Exception {
    rx.orderByValue();

    then(query).should().orderByValue();
  }

  @Test
  @SuppressLint("CheckResult")
  public void shouldLimitToFirst() throws Exception {
    rx.limitToFirst(100);

    then(query).should().limitToFirst(100);
  }

  @Test
  @SuppressLint("CheckResult")
  public void shouldLimitToLast() throws Exception {
    rx.limitToLast(50);

    then(query).should().limitToLast(50);
  }

  @Test
  public void shouldSetPriority() throws Exception {
    given(reference.setPriority("priority")).willReturn(task);

    rx.setPriority("priority").subscribe();

    then(reference).should().setPriority("priority");
  }

  @Test
  public void shouldSetValueWithNullPriority() throws Exception {
    rx.setValue(null).subscribe();

    then(reference).should().setValue(isNull(), isNull(), any(DatabaseReference.CompletionListener.class));
  }

  @Test
  public void shouldSetValue() throws Exception {
    rx.setValue("string").subscribe();

    then(reference).should().setValue(Matchers.eq("string"), isNull(), any(DatabaseReference.CompletionListener.class));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void shouldUpdateChildren() throws Exception {
    Map<String, Object> map = mock(Map.class);
    rx.updateChildren(map).subscribe();

    then(reference).should().updateChildren(map);
  }

  @Test
  public void shouldRemoveValue() throws Exception {
    rx.removeValue().subscribe();

    then(reference).should().removeValue();
  }

  @Test
  public void shouldAddValueEventListener() throws Exception {
    rx.onValueEvent().subscribe();

    then(query).should().addValueEventListener(any(ValueEventListener.class));
  }

  @Test
  public void shouldAddSingleValueEventListener() throws Exception {
    rx.onSingleValueEvent().subscribe();

    then(query).should().addListenerForSingleValueEvent(any(ValueEventListener.class));
  }

  @Test
  public void shouldAddChildEventListener() throws Exception {
    rx.onChildEvent().subscribe();

    then(query).should().addChildEventListener(any(ChildEventListener.class));
  }

  @Test
  public void shouldBufferChildEvents() throws Exception {
    ArgumentCaptor<ChildEventListener> captor = forClass(ChildEventListener.class);
    TestSubscriber<ChildEvent> subscriber = rx.onChildEvent().test(1);
    then(query).should().addChildEventListener(captor.capture());

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
    then(query).should().addChildEventListener(captor.capture());

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

    then(query).should().addChildEventListener(captor.capture());

    ChildEventListener listener = captor.getValue();
    DataSnapshot snapshot = mock(DataSnapshot.class);

    given(snapshot.getValue(String.class)).willReturn("biscuits");
    listener.onChildAdded(snapshot, null);

    subscriber
        .assertValue("biscuits")
        .assertNoErrors();
  }

  @Test
  public void shouldPurgeOutstandingWrites() throws Exception {
    rx.purgeOutstandingWrites();

    then(database).should().purgeOutstandingWrites();
  }

  @Test
  public void shouldGoOnline() throws Exception {
    rx.goOnline();

    then(database).should().goOnline();
  }

  @Test
  public void shouldGoOffline() throws Exception {
    rx.goOffline();

    then(database).should().goOffline();
  }

  @Test
  public void shouldSetLogLevel() throws Exception {
    rx.setLogLevel(Logger.Level.DEBUG);

    then(database).should().setLogLevel(Logger.Level.DEBUG);
  }
}
