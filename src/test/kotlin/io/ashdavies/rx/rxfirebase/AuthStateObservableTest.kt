package io.ashdavies.rx.rxfirebase

import com.google.firebase.auth.FirebaseAuth
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import io.reactivex.ObservableEmitter
import org.junit.jupiter.api.Test

internal class AuthStateObservableTest {

  private val emitter: ObservableEmitter<FirebaseAuth> = mock()
  private val auth: FirebaseAuth = mock()

  private val observable = AuthStateObservable(emitter)

  @Test
  fun `should invoke on next`() {
    observable.onAuthStateChanged(auth)

    then(emitter)
        .should()
        .onNext(auth)
  }
}
