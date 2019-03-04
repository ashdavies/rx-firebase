package io.ashdavies.rx.rxfirebase

import androidx.annotation.CheckResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ProviderQueryResult
import com.google.firebase.auth.SignInMethodQueryResult
import io.ashdavies.rx.extensions.onAuthState
import io.ashdavies.rx.rxtasks.toCompletable
import io.ashdavies.rx.rxtasks.toSingle
import io.reactivex.BackpressureStrategy
import io.reactivex.BackpressureStrategy.BUFFER
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Deprecated("Use RxTasks instead")
data class RxFirebaseAuth(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {

  @CheckResult
  fun currentUser(): Maybe<FirebaseUser> = Maybe.fromCallable { auth.currentUser }

  @CheckResult
  @JvmOverloads
  fun authStateChanged(strategy: BackpressureStrategy = BUFFER): Flowable<FirebaseAuth> = auth
      .onAuthState()
      .toFlowable(strategy)

  @CheckResult
  fun signInWithCredential(credential: AuthCredential): Single<AuthResult> = auth
      .signInWithCredential(credential)
      .toSingle()

  @CheckResult
  fun signInWithCustomToken(token: String): Single<AuthResult> = auth
      .signInWithCustomToken(token)
      .toSingle()

  @CheckResult
  fun signInWithEmailAndPassword(email: String, password: String): Single<AuthResult> = auth
      .signInWithEmailAndPassword(email, password)
      .toSingle()

  @CheckResult
  fun signInAnonymously(): Single<AuthResult> = auth
      .signInAnonymously()
      .toSingle()

  @CheckResult
  fun createUserWithEmailAndPassword(email: String, password: String): Single<AuthResult> = auth
      .createUserWithEmailAndPassword(email, password)
      .toSingle()

  @CheckResult
  @Deprecated("Use fetchSignInMethodsForEmail instead")
  fun fetchProvidersForEmail(email: String): Single<ProviderQueryResult> = auth
      .fetchProvidersForEmail(email)
      .toSingle()

  @CheckResult
  fun fetchSignInMethodsForEmail(email: String): Single<SignInMethodQueryResult> = auth
      .fetchSignInMethodsForEmail(email)
      .toSingle()

  @CheckResult
  fun sendPasswordResetEmail(email: String): Completable = auth
      .sendPasswordResetEmail(email)
      .toCompletable()

  @CheckResult
  fun signOut(): RxFirebaseAuth = apply { auth.signOut() }

  companion object {

    @JvmStatic
    fun getInstance() = RxFirebaseAuth()

    @JvmStatic
    fun getInstance(auth: FirebaseAuth) = RxFirebaseAuth(auth)
  }
}
