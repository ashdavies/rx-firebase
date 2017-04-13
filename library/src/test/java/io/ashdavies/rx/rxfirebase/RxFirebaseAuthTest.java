package io.ashdavies.rx.rxfirebase;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class RxFirebaseAuthTest {

  private RxFirebaseAuth rx;

  @Mock FirebaseAuth auth;

  @Before
  public void setUp() throws Exception {
    rx = RxFirebaseAuth.getInstance(auth);
  }

  @Test
  public void shouldAddAuthStateListener() throws Exception {
    rx.authStateChanged().subscribe();

    then(auth).should().addAuthStateListener(any(FirebaseAuth.AuthStateListener.class));
  }

  @Test
  public void shouldSignInWithCredentials() throws Exception {
    AuthCredential credential = mock(AuthCredential.class);

    rx.signInWithCredential(credential).subscribe();

    then(auth).should().signInWithCredential(credential);
  }

  @Test
  public void shouldSignInWithCustomToken() throws Exception {
    rx.signInWithCustomToken("token").subscribe();

    then(auth).should().signInWithCustomToken("token");
  }

  @Test
  public void shouldSignInWithEmailAndPassword() throws Exception {
    rx.signInWithEmailAndPassword("email", "password").subscribe();

    then(auth).should().signInWithEmailAndPassword("email", "password");
  }

  @Test
  public void shouldSignInAnonymously() throws Exception {
    rx.signInAnonymously().subscribe();

    then(auth).should().signInAnonymously();
  }

  @Test
  public void shouldCreateUserWithEmailAndPassword() throws Exception {
    rx.createUserWithEmailAndPassword("email", "password").subscribe();

    then(auth).should().createUserWithEmailAndPassword("email", "password");
  }

  @Test
  public void shouldFetchProvidersForEmail() throws Exception {
    rx.fetchProvidersForEmail("email").subscribe();

    then(auth).should().fetchProvidersForEmail("email");
  }

  @Test
  public void shouldGetCurrentUser() throws Exception {
    given(auth.getCurrentUser()).willReturn(mock(FirebaseUser.class));

    rx.getCurrentUser().test()
        .assertValueCount(1)
        .assertComplete();
  }

  @Test
  public void shouldSendPasswordResetEmail() throws Exception {
    rx.sendPasswordResetEmail("email").subscribe();

    then(auth).should().sendPasswordResetEmail("email");
  }

  @Test
  public void shouldSignOut() throws Exception {
    assertThat(rx.signOut()).isEqualTo(rx);

    then(auth).should().signOut();
  }

  @Test
  public void shouldNotGetCurrentUser() throws Exception {
    rx.getCurrentUser().test()
        .assertNoValues()
        .assertComplete();
  }
}
