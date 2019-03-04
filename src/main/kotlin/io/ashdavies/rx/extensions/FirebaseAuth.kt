package io.ashdavies.rx.extensions

import com.google.firebase.auth.FirebaseAuth
import io.ashdavies.rx.rxfirebase.AuthStateObservable
import io.reactivex.Observable

fun FirebaseAuth.onAuthState(): Observable<FirebaseAuth> = Observable.create {
  val listener = AuthStateObservable(it)
  it.setCancellable { removeAuthStateListener(listener) }
  addAuthStateListener(listener)
}
