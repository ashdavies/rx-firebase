package io.ashdavies.rx.extensions

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.nhaarman.mockito_kotlin.KArgumentCaptor
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class FirebaseAuthTest {

  private val auth: FirebaseAuth = mock()
  private val captor: KArgumentCaptor<AuthStateListener> = argumentCaptor()

  private val listener: AuthStateListener
    get() = captor.lastValue

  @BeforeEach
  fun setUp() {
    verify(auth).addAuthStateListener(captor.capture())
  }

  @Test
  fun `should emit firebase auth`() {
    val observer: TestObserver<FirebaseAuth> = auth
        .onAuthState()
        .test()

    listener.onAuthStateChanged(auth)

    observer.assertValue(auth)
  }

  @Test
  fun `should remove auth state listener on dispose`() {
    auth
        .onAuthState()
        .test()
        .dispose()

    then(auth)
        .should()
        .removeAuthStateListener(listener)
  }
}
