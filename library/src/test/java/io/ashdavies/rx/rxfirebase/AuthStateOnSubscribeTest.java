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

import static org.mockito.Mockito.verify;

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
    verify(auth).addAuthStateListener(captor.capture());

    captor.getValue().onAuthStateChanged(auth);
    verify(emitter).onNext(auth);
  }

  @Test
  public void shouldRemoveAuthStateListenerOnCancel() throws Exception {
    ArgumentCaptor<Cancellable> captor = ArgumentCaptor.forClass(Cancellable.class);

    onSubscribe.subscribe(emitter);
    verify(emitter).setCancellable(captor.capture());
    verify(auth).addAuthStateListener(this.captor.capture());

    captor.getValue().cancel();
    verify(auth).removeAuthStateListener(this.captor.getValue());
  }
}
