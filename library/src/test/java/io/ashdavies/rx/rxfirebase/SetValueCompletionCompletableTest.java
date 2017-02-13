package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.CompletableEmitter;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SetValueCompletionCompletableTest {

  private SetValueCompletionCompletable completable;

  @Mock CompletableEmitter emitter;

  @Mock DatabaseReference reference;
  @Mock DatabaseException exception;
  @Mock DatabaseError error;

  @Before
  public void setUp() throws Exception {
    completable = new SetValueCompletionCompletable(emitter);
  }

  @Test
  public void shouldNotEmitIfCancelled() throws Exception {
    given(emitter.isDisposed()).willReturn(true);

    completable.onComplete(null, reference);
    completable.onComplete(error, null);

    verify(emitter, never()).onError(any(Throwable.class));
    verify(emitter, never()).onComplete();
  }

  @Test
  public void shouldEmitErrorException() throws Exception {
    given(error.toException()).willReturn(exception);

    completable.onComplete(error, null);

    verify(emitter).onError(exception);
    verify(emitter, never()).onComplete();
  }

  @Test
  public void shouldEmitOnComplete() throws Exception {
    completable.onComplete(null, reference);

    verify(emitter, never()).onError(any(Throwable.class));
    verify(emitter).onComplete();
  }
}
