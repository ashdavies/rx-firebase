package io.ashdavies.rx.rxfirebase

import com.google.firebase.auth.FirebaseAuth
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import io.reactivex.FlowableEmitter
import org.junit.jupiter.api.Test

internal class AuthStateFlowableTest {

  private val emitter: FlowableEmitter<FirebaseAuth> = mock()
  private val auth: FirebaseAuth = mock()

  private val flowable = AuthStateFlowable(emitter)

  @Test
  fun `should invoke on next`() {
    flowable.onAuthStateChanged(auth)

    then(emitter)
        .should()
        .onNext(auth)
  }
}
