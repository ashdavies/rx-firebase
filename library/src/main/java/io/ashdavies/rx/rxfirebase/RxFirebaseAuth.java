package io.ashdavies.rx.rxfirebase;

import android.support.annotation.CheckResult;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;

import io.ashdavies.rx.common.NullableMaybeOnSubscribe;
import io.ashdavies.rx.rxtasks.RxTasks;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@SuppressWarnings("WeakerAccess")
public final class RxFirebaseAuth {
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

  @CheckResult
  public Flowable<FirebaseAuth> authStateChanged() {
    return Flowable.create(new AuthStateOnSubscribe(auth), BackpressureStrategy.BUFFER);
  }

  @CheckResult
  public Single<AuthResult> signInWithCredential(AuthCredential authCredential) {
    return RxTasks.single(auth.signInWithCredential(authCredential));
  }

  @CheckResult
  public Single<AuthResult> signInWithCustomToken(String token) {
    return RxTasks.single(auth.signInWithCustomToken(token));
  }

  @CheckResult
  public Single<AuthResult> signInWithEmailAndPassword(String email, String password) {
    return RxTasks.single(auth.signInWithEmailAndPassword(email, password));
  }

  @CheckResult
  public Single<AuthResult> signInAnonymously() {
    return RxTasks.single(auth.signInAnonymously());
  }

  @CheckResult
  public Single<AuthResult> createUserWithEmailAndPassword(String email, String password) {
    return RxTasks.single(auth.createUserWithEmailAndPassword(email, password));
  }

  @CheckResult
  public Single<ProviderQueryResult> fetchProvidersForEmail(String email) {
    return RxTasks.single(auth.fetchProvidersForEmail(email));
  }

  @CheckResult
  public Maybe<FirebaseUser> getCurrentUser() {
    return Maybe.create(new NullableMaybeOnSubscribe<>(auth.getCurrentUser()));
  }

  @CheckResult
  public Completable sendPasswordResetEmail(String email) {
    return RxTasks.completable(auth.sendPasswordResetEmail(email));
  }

  public RxFirebaseAuth signOut() {
    auth.signOut();
    return this;
  }
}
