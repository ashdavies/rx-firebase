package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Cancellable;

import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ValueEventOnSubscribeTest {

  private FlowableOnSubscribe<DataSnapshot> onSubscribe;

  @Captor ArgumentCaptor<ValueEventListener> captor;

  @Mock Query query;
  @Mock FlowableEmitter<DataSnapshot> emitter;

  @Mock DataSnapshot snapshot;
  @Mock DatabaseError error;

  @Before
  public void setUp() throws Exception {
    onSubscribe = new ValueEventOnSubscribe(query);
  }

  @Test
  public void shouldEmitValue() throws Exception {
    onSubscribe.subscribe(emitter);
    verify(query).addValueEventListener(captor.capture());

    captor.getValue().onDataChange(snapshot);
    verify(emitter).onNext(snapshot);
  }

  @Test
  public void shouldEmitSingleError() throws Exception {
    onSubscribe.subscribe(emitter);
    verify(query).addValueEventListener(captor.capture());

    DatabaseException exception = mock(DatabaseException.class);
    given(error.toException()).willReturn(exception);

    captor.getValue().onCancelled(error);
    verify(emitter).onError(exception);
  }

  @Test
  public void shouldRemoveValueEventListenerOnCancel() throws Exception {
    ArgumentCaptor<Cancellable> captor = forClass(Cancellable.class);

    onSubscribe.subscribe(emitter);
    verify(emitter).setCancellable(captor.capture());
    verify(query).addValueEventListener(this.captor.capture());

    captor.getValue().cancel();
    verify(query).removeEventListener(this.captor.getValue());
  }
}
