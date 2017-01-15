package io.ashdavies.rx.rxfirebase;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

import io.ashdavies.rx.rxtasks.RxTasks;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Single;

@SuppressWarnings("WeakerAccess")
public class RxFirebaseAuth {
  private final FirebaseAuth auth;

  private RxFirebaseAuth(FirebaseAuth auth) {
    this.auth = auth;
  }

  public static RxFirebaseAuth getInstance() {
    return getInstance(FirebaseAuth.getInstance());
  }

  public static RxFirebaseAuth getInstance(FirebaseAuth firebaseAuth) {
    return new RxFirebaseAuth(firebaseAuth);
  }

  public Flowable<FirebaseAuth> authStateChanged() {
    return Flowable.create(new AuthStateOnSubscribe(auth), FlowableEmitter.BackpressureMode.BUFFER);
  }

  public Single<AuthResult> signInWithCredential(AuthCredential authCredential) {
    return RxTasks.single(auth.signInWithCredential(authCredential));
  }

  public Single<AuthResult> signInWithCustomToken(String token) {
    return RxTasks.single(auth.signInWithCustomToken(token));
  }

  public Single<AuthResult> signInWithEmailAndPassword(String email, String password) {
    return RxTasks.single(auth.signInWithEmailAndPassword(email, password));
  }

  public Single<AuthResult> signInAnonymously() {
    return RxTasks.single(auth.signInAnonymously());
  }

  public Single<AuthResult> createUserWithEmailAndPassword(String email, String password) {
    return RxTasks.single(auth.createUserWithEmailAndPassword(email, password));
  }

  public Single<ProviderQueryResult> createUserWithEmailAndPassword(String email) {
    return RxTasks.single(auth.fetchProvidersForEmail(email));
  }

  public Completable sendPasswordResetEmail(String email) {
    return RxTasks.completable(auth.sendPasswordResetEmail(email));
  }

  public RxFirebaseAuth signOut() {
    auth.signOut();
    return this;
  }
}
