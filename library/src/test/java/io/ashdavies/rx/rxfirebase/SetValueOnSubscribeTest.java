package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.DatabaseReference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.CompletableEmitter;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;

@RunWith(MockitoJUnitRunner.class)
public class SetValueOnSubscribeTest {

  private static final String DIGESTIVES = "DIGESTIVES";

  private SetValueOnSubscribe onSubscribe;

  @Captor ArgumentCaptor<DatabaseReference.CompletionListener> captor;

  @Mock DatabaseReference reference;
  @Mock CompletableEmitter emitter;

  @Before
  public void setUp() throws Exception {
    onSubscribe = new SetValueOnSubscribe(reference, DIGESTIVES, null);
  }

  @Test
  public void shouldSetValueListener() throws Exception {
    given(emitter.isDisposed()).willReturn(false);

    onSubscribe.subscribe(emitter);
    then(reference).should().setValue(eq(DIGESTIVES), isNull(), captor.capture());

    captor.getValue().onComplete(null, reference);
    then(emitter).should().onComplete();
  }
}
