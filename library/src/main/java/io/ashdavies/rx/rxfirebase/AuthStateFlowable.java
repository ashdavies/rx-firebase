package io.ashdavies.rx.rxfirebase;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.FlowableEmitter;
import io.reactivex.functions.Cancellable;

class AuthStateFlowable implements FirebaseAuth.AuthStateListener, TypeCancellable<FirebaseAuth> {

  private final FlowableEmitter<FirebaseAuth> emitter;

  AuthStateFlowable(FlowableEmitter<FirebaseAuth> emitter) {
    this.emitter = emitter;
  }

  @Override
  public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
    emitter.onNext(auth);
  }

  @Override
  public Cancellable cancellable(FirebaseAuth auth) {
    return new AuthStateCancellable(auth, this);
  }
}
