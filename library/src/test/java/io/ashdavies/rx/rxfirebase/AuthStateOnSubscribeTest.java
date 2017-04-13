package io.ashdavies.rx.rxfirebase;

import com.google.firebase.auth.FirebaseAuth;

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
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class AuthStateOnSubscribeTest {

  private FlowableOnSubscribe<FirebaseAuth> onSubscribe;

  @Captor ArgumentCaptor<FirebaseAuth.AuthStateListener> captor;

  @Mock FirebaseAuth auth;
  @Mock FlowableEmitter<FirebaseAuth> emitter;

  @Before
  public void setUp() throws Exception {
    onSubscribe = new AuthStateOnSubscribe(auth);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void shouldAddAuthStateListener() throws Exception {
    onSubscribe.subscribe(emitter);
    then(auth).should().addAuthStateListener(captor.capture());

    captor.getValue().onAuthStateChanged(auth);
    then(emitter).should().onNext(auth);
  }

  @Test
  public void shouldRemoveAuthStateListenerOnCancel() throws Exception {
    ArgumentCaptor<Cancellable> captor = forClass(Cancellable.class);

    onSubscribe.subscribe(emitter);
    then(emitter).should().setCancellable(captor.capture());
    then(auth).should().addAuthStateListener(this.captor.capture());

    captor.getValue().cancel();
    then(auth).should().removeAuthStateListener(this.captor.getValue());
  }
}
