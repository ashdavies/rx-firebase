package io.ashdavies.rx.rxfirebase;

import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

class AuthStateOnSubscribe implements FlowableOnSubscribe<FirebaseAuth> {

  private final FirebaseAuth auth;

  AuthStateOnSubscribe(FirebaseAuth auth) {
    this.auth = auth;
  }

  @Override
  public void subscribe(FlowableEmitter<FirebaseAuth> emitter) throws Exception {
    AuthStateFlowable listener = new AuthStateFlowable(emitter);
    emitter.setCancellable(listener.cancellable(auth));
    auth.addAuthStateListener(listener);
  }
}
