package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.Query;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.FlowableEmitter;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FlowableChildEventListenerTest {

  private static final String PREVIOUS = "previous";

  private FlowableChildEventListener listener;

  @Captor ArgumentCaptor<ChildEvent> captor;

  @Mock FlowableEmitter<ChildEvent> emitter;
  @Mock DataSnapshot snapshot;
  @Mock Query query;

  @Before
  public void setUp() throws Exception {
    listener = new FlowableChildEventListener(emitter);
  }

  @Test
  public void shouldEmitOnChildAdded() throws Exception {
    listener.onChildAdded(snapshot, PREVIOUS);
    verify(emitter).onNext(captor.capture());

    assertThat(captor.getValue().snapshot()).isEqualTo(snapshot);
    assertThat(captor.getValue().type()).isEqualTo(ChildEvent.Type.CHILD_ADDED);
  }

  @Test
  public void shouldEmitOnChildChanged() throws Exception {
    listener.onChildChanged(snapshot, PREVIOUS);
    verify(emitter).onNext(captor.capture());

    assertThat(captor.getValue().snapshot()).isEqualTo(snapshot);
    assertThat(captor.getValue().type()).isEqualTo(ChildEvent.Type.CHILD_CHANGED);
  }

  @Test
  public void shouldEmitOnChildRemoved() throws Exception {
    listener.onChildRemoved(snapshot);
    verify(emitter).onNext(captor.capture());

    assertThat(captor.getValue().snapshot()).isEqualTo(snapshot);
    assertThat(captor.getValue().type()).isEqualTo(ChildEvent.Type.CHILD_REMOVED);
  }

  @Test
  public void shouldEmitOnChildMoved() throws Exception {
    listener.onChildMoved(snapshot, PREVIOUS);
    verify(emitter).onNext(captor.capture());

    assertThat(captor.getValue().snapshot()).isEqualTo(snapshot);
    assertThat(captor.getValue().type()).isEqualTo(ChildEvent.Type.CHILD_MOVED);
  }

  @Test
  public void shouldEmitDatabaseError() throws Exception {
    DatabaseError error = mock(DatabaseError.class);
    DatabaseException exception = mock(DatabaseException.class);
    given(error.toException()).willReturn(exception);

    listener.onCancelled(error);

    verify(emitter).onError(exception);
  }

  @Test
  public void shouldRemoveEventListener() throws Exception {
    listener.cancellable(query).cancel();

    verify(query).removeEventListener(listener);
  }
}
