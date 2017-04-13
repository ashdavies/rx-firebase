package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.Query;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.FlowableEmitter;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ValueEventFlowableTest {

  private ValueEventFlowable flowable;

  @Mock FlowableEmitter<DataSnapshot> emitter;

  @Before
  public void setUp() throws Exception {
    flowable = new ValueEventFlowable(emitter);
  }

  @Test
  public void shouldEmitDataSnapshot() throws Exception {
    DataSnapshot snapshot = mock(DataSnapshot.class);

    flowable.onDataChange(snapshot);

    then(emitter).should().onNext(snapshot);
  }

  @Test
  public void shouldEmitException() throws Exception {
    DatabaseError error = mock(DatabaseError.class);

    DatabaseException exception = mock(DatabaseException.class);
    given(error.toException()).willReturn(exception);

    flowable.onCancelled(error);

    then(emitter).should().onError(exception);
  }

  @Test
  public void shouldRemoveEventListener() throws Exception {
    Query query = mock(Query.class);

    flowable.cancellable(query).cancel();

    then(query).should().removeEventListener(flowable);
  }
}
