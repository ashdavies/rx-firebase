package io.ashdavies.rx.rxfirebase;

import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.functions.Cancellable;

class AuthStateCancellable implements Cancellable {

  private final FirebaseAuth auth;
  private final FirebaseAuth.AuthStateListener listener;

  AuthStateCancellable(FirebaseAuth auth, FirebaseAuth.AuthStateListener listener) {
    this.auth = auth;
    this.listener = listener;
  }

  @Override
  public void cancel() throws Exception {
    auth.removeAuthStateListener(listener);
  }
}
