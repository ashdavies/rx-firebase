package io.ashdavies.rx.rxfirebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import io.reactivex.ObservableEmitter

internal class AuthStateObservable(private val emitter: ObservableEmitter<FirebaseAuth>) : AuthStateListener {

  override fun onAuthStateChanged(auth: FirebaseAuth) = emitter.onNext(auth)
}
