package io.ashdavies.rx.rxfirebase;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.functions.Cancellable;

import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class AuthStateCancellableTest {

  private Cancellable cancellable;

  @Mock FirebaseAuth auth;
  @Mock FirebaseAuth.AuthStateListener listener;

  @Before
  public void setUp() throws Exception {
    cancellable = new AuthStateCancellable(auth, listener);
  }

  @Test
  public void shouldRemoveAuthStateListener() throws Exception {
    cancellable.cancel();

    then(auth).should().removeAuthStateListener(listener);
  }
}
