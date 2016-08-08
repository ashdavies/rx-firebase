package io.ashdavies.rxfirebase.auth;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import rx.AsyncEmitter;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public final class RxFirebaseAuth {
  private final FirebaseAuth firebase;
  private final AsyncEmitter.BackpressureMode mode;

  private RxFirebaseAuth(FirebaseAuth firebase, AsyncEmitter.BackpressureMode mode) {
    this.firebase = firebase;
    this.mode = mode;
  }

  public static RxFirebaseAuth getInstance() {
    return getInstance(FirebaseAuth.getInstance());
  }

  public static RxFirebaseAuth getInstance(FirebaseAuth firebase) {
    return getInstance(firebase, AsyncEmitter.BackpressureMode.LATEST);
  }

  public static RxFirebaseAuth getInstance(FirebaseAuth firebase, AsyncEmitter.BackpressureMode mode) {
    return new RxFirebaseAuth(firebase, mode);
  }

  public Observable<FirebaseUser> getUser() {
    return Observable.concat(getCurrentUser(), getUserOnAuthStateChange())
        .first(new Func1<FirebaseUser, Boolean>() {
          @Override public Boolean call(FirebaseUser user) {
            return user != null;
          }
        });
  }

  private Observable<FirebaseUser> getCurrentUser() {
    return Observable.just(firebase.getCurrentUser());
  }

  private Observable<FirebaseUser> getUserOnAuthStateChange() {
    return onAuthStateChange().map(new Func1<FirebaseAuth, FirebaseUser>() {
      @Override public FirebaseUser call(FirebaseAuth auth) {
        return auth.getCurrentUser();
      }
    });
  }

  public Observable<FirebaseAuth> onAuthStateChange() {
    return Observable.fromAsync(new Action1<AsyncEmitter<FirebaseAuth>>() {
      @Override public void call(final AsyncEmitter<FirebaseAuth> emitter) {
        final FirebaseAuth.AuthStateListener listener = new FirebaseAuth.AuthStateListener() {
          @Override public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
            emitter.onNext(auth);
          }
        };

        emitter.setCancellation(new AsyncEmitter.Cancellable() {
          @Override public void cancel() throws Exception {
            firebase.removeAuthStateListener(listener);
          }
        });

        firebase.addAuthStateListener(listener);
      }
    }, mode);
  }

  public Observable<Void> updateEmail(final String email) {
    return getCurrentUser().flatMap(new Func1<FirebaseUser, Observable<Void>>() {
      @Override public Observable<Void> call(FirebaseUser user) {
        return RxUtils.from(user.updateEmail(email), mode);
      }
    });
  }

  public Observable<Void> updatePassword(final String email) {
    return getCurrentUser().flatMap(new Func1<FirebaseUser, Observable<Void>>() {
      @Override public Observable<Void> call(FirebaseUser user) {
        return RxUtils.from(user.updatePassword(email), mode);
      }
    });
  }

  public Observable<Void> updateProfile(final UserProfileChangeRequest request) {
    return getCurrentUser().flatMap(new Func1<FirebaseUser, Observable<Void>>() {
      @Override public Observable<Void> call(FirebaseUser user) {
        return RxUtils.from(user.updateProfile(request), mode);
      }
    });
  }

  public Observable<Void> sendPasswordResetEmail(final String email) {
    return RxUtils.from(firebase.sendPasswordResetEmail(email));
  }
}
