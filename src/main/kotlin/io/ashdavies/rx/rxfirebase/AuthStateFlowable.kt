package io.ashdavies.rx.rxfirebase

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.FlowableEmitter

internal class AuthStateFlowable(private val emitter: FlowableEmitter<FirebaseAuth>) : FirebaseAuth.AuthStateListener {

  override fun onAuthStateChanged(auth: FirebaseAuth) = emitter.onNext(auth)
}
