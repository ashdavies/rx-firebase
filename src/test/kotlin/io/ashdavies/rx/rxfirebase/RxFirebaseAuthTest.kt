package io.ashdavies.rx.rxfirebase

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ProviderQueryResult
import com.google.firebase.auth.SignInMethodQueryResult
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import org.junit.jupiter.api.Test

internal class RxFirebaseAuthTest {

  private val result: AuthResult = mock()
  private val delegate: FirebaseAuth = mock()

  private val auth = RxFirebaseAuth(delegate)

  @Test
  fun `should return current user`() {
    val user: FirebaseUser = mock()

    given(delegate.currentUser).willReturn(user)

    auth
        .currentUser()
        .test()
        .assertResult(user)
  }

  @Test
  fun `should sign in with credential`() {
    val credential: AuthCredential = mock()

    given(delegate.signInWithCredential(credential)).willReturn(Tasks.forResult(result))

    auth
        .signInWithCredential(credential)
        .test()
        .assertResult(result)
  }

  @Test
  fun `should sign in with custom token`() {
    given(delegate.signInWithCustomToken("CUSTOM_TOKEN")).willReturn(Tasks.forResult(result))

    auth
        .signInWithCustomToken("CUSTOM_TOKEN")
        .test()
        .assertResult(result)
  }

  @Test
  fun `should sign in with email and password`() {
    given(delegate.signInWithEmailAndPassword("EMAIL", "PASSWORD")).willReturn(Tasks.forResult(result))

    auth
        .signInWithEmailAndPassword("EMAIL", "PASSWORD")
        .test()
        .assertResult(result)
  }

  @Test
  fun `should sign in anonymously`() {
    given(delegate.signInAnonymously()).willReturn(Tasks.forResult(result))

    auth
        .signInAnonymously()
        .test()
        .assertResult(result)
  }

  @Test
  fun `should create user with email and password`() {
    given(delegate.createUserWithEmailAndPassword("EMAIL", "PASSWORD")).willReturn(Tasks.forResult(result))

    auth
        .createUserWithEmailAndPassword("EMAIL", "PASSWORD")
        .test()
        .assertResult(result)
  }

  @Test
  fun `should fetch providers for email`() {
    val result: ProviderQueryResult = mock()

    given(delegate.fetchProvidersForEmail("EMAIL")).willReturn(Tasks.forResult(result))

    auth
        .fetchProvidersForEmail("EMAIL")
        .test()
        .assertResult(result)
  }

  @Test
  fun `should fetch sign in methods for email`() {
    val result: SignInMethodQueryResult = mock()

    given(delegate.fetchSignInMethodsForEmail("EMAIL")).willReturn(Tasks.forResult(result))

    auth
        .fetchSignInMethodsForEmail("EMAIL")
        .test()
        .assertResult(result)
  }

  @Test
  fun `should send password reset email`() {
    given(delegate.sendPasswordResetEmail("EMAIL")).willReturn(Tasks.forResult(null))

    auth
        .sendPasswordResetEmail("EMAIL")
        .test()
        .assertResult()
  }

  @Test
  fun `should sign out user`() {
    auth.signOut()

    then(delegate)
        .should()
        .signOut()
  }
}
