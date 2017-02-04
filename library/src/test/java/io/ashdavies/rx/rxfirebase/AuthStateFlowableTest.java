package io.ashdavies.rx.rxfirebase;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.FlowableEmitter;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuthStateFlowableTest {

  private AuthStateFlowable flowable;

  @Mock FirebaseAuth auth;
  @Mock FlowableEmitter<FirebaseAuth> emitter;

  @Before
  public void setUp() throws Exception {
    flowable = new AuthStateFlowable(emitter);
  }

  @Test
  public void shouldEmitAuthStateChange() throws Exception {
    flowable.onAuthStateChanged(auth);

    verify(emitter).onNext(auth);
  }

  @Test
  public void shouldRemoveAuthStateListener() throws Exception {
    flowable.cancellable(auth).cancel();

    verify(auth).removeAuthStateListener(flowable);
  }
}
