package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.functions.Cancellable;

import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class ValueEventCancellableTest {

  private Cancellable cancellable;

  @Mock Query query;
  @Mock ValueEventListener listener;

  @Before
  public void setUp() throws Exception {
    cancellable = new ValueEventCancellable(query, listener);
  }

  @Test
  public void shouldRemoveEventListener() throws Exception {
    cancellable.cancel();
    then(query).should().removeEventListener(listener);
  }
}
