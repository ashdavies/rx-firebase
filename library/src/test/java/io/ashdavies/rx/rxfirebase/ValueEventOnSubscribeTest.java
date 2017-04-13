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
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

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
    then(query).should().addValueEventListener(captor.capture());

    captor.getValue().onDataChange(snapshot);
    then(emitter).should().onNext(snapshot);
  }

  @Test
  public void shouldEmitSingleError() throws Exception {
    onSubscribe.subscribe(emitter);
    then(query).should().addValueEventListener(captor.capture());

    DatabaseException exception = mock(DatabaseException.class);
    given(error.toException()).willReturn(exception);

    captor.getValue().onCancelled(error);
    then(emitter).should().onError(exception);
  }

  @Test
  public void shouldRemoveValueEventListenerOnCancel() throws Exception {
    ArgumentCaptor<Cancellable> captor = forClass(Cancellable.class);

    onSubscribe.subscribe(emitter);
    then(emitter).should().setCancellable(captor.capture());
    then(query).should().addValueEventListener(this.captor.capture());

    captor.getValue().cancel();
    then(query).should().removeEventListener(this.captor.getValue());
  }
}
