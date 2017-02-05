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

import io.reactivex.SingleEmitter;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ValueEventSingleTest {

  private ValueEventSingle single;

  @Mock SingleEmitter<DataSnapshot> emitter;

  @Before
  public void setUp() throws Exception {
    single = new ValueEventSingle(emitter);
  }

  @Test
  public void shouldEmitDataSnapshot() throws Exception {
    DataSnapshot snapshot = mock(DataSnapshot.class);

    single.onDataChange(snapshot);

    verify(emitter).onSuccess(snapshot);
  }

  @Test
  public void shouldEmitException() throws Exception {
    DatabaseError error = mock(DatabaseError.class);

    DatabaseException exception = mock(DatabaseException.class);
    given(error.toException()).willReturn(exception);

    single.onCancelled(error);

    verify(emitter).onError(exception);
  }

  @Test
  public void shouldRemoveEventListener() throws Exception {
    Query query = mock(Query.class);

    single.cancellable(query).cancel();

    verify(query).removeEventListener(single);
  }
}
