package io.ashdavies.rxfirebase;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.core.view.Event;

import java.util.Map;

import rx.AsyncEmitter;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public final class RxFirebase {
  private final Firebase firebase;
  private final AsyncEmitter.BackpressureMode mode;

  private RxFirebase(final Firebase firebase, final AsyncEmitter.BackpressureMode mode) {
    this.firebase = firebase;
    this.mode = mode;
  }

  public static RxFirebase with(final Firebase firebase) {
    return with(firebase, AsyncEmitter.BackpressureMode.LATEST);
  }

  public static RxFirebase with(final Firebase firebase, final AsyncEmitter.BackpressureMode mode) {
    return new RxFirebase(firebase, mode);
  }

  public Observable<AuthData> authAnonymously() {
    return Observable.fromAsync(
        new Action1<AsyncEmitter<AuthData>>() {
          @Override
          public void call(final AsyncEmitter<AuthData> emitter) {
            firebase.authAnonymously(
                new Firebase.AuthResultHandler() {
                  @Override
                  public void onAuthenticated(final AuthData data) {
                    emitter.onNext(data);
                    emitter.onCompleted();
                  }

                  @Override
                  public void onAuthenticationError(final FirebaseError error) {
                    emitter.onError(error.toException());
                  }
                });
          }
        },
        mode);
  }

  public Observable<AuthData> authWithPassword(final String email, final String password) {
    return Observable.fromAsync(
        new Action1<AsyncEmitter<AuthData>>() {
          @Override
          public void call(final AsyncEmitter<AuthData> emitter) {
            firebase.authWithPassword(
                email,
                password,
                new Firebase.AuthResultHandler() {
                  @Override
                  public void onAuthenticated(final AuthData data) {
                    emitter.onNext(data);
                    emitter.onCompleted();
                  }

                  @Override
                  public void onAuthenticationError(final FirebaseError error) {
                    emitter.onError(error.toException());
                  }
                });
          }
        },
        mode);
  }

  public final Observable<AuthData> authWithOAuthToken(final String provider, final String token) {
    return Observable.fromAsync(
        new Action1<AsyncEmitter<AuthData>>() {
          @Override
          public void call(final AsyncEmitter<AuthData> emitter) {
            firebase.authWithOAuthToken(
                provider,
                token,
                new Firebase.AuthResultHandler() {
                  @Override
                  public void onAuthenticated(final AuthData data) {
                    emitter.onNext(data);
                    emitter.onCompleted();
                  }

                  @Override
                  public void onAuthenticationError(final FirebaseError error) {
                    emitter.onError(error.toException());
                  }
                });
          }
        },
        mode);
  }

  public final Observable<Map<String, Object>> createUser(
      final String email, final String password) {
    return Observable.fromAsync(
        new Action1<AsyncEmitter<Map<String, Object>>>() {
          @Override
          public void call(final AsyncEmitter<Map<String, Object>> emitter) {
            firebase.createUser(
                email,
                password,
                new Firebase.ValueResultHandler<Map<String, Object>>() {
                  @Override
                  public void onSuccess(final Map<String, Object> result) {
                    emitter.onNext(result);
                    emitter.onCompleted();
                  }

                  @Override
                  public void onError(FirebaseError error) {
                    emitter.onError(error.toException());
                  }
                });
          }
        },
        mode);
  }

  public final Observable<AuthData> onAuthStateEvent() {
    return Observable.fromAsync(
        new Action1<AsyncEmitter<AuthData>>() {
          @Override
          public void call(final AsyncEmitter<AuthData> emitter) {
            final Firebase.AuthStateListener listener =
                new Firebase.AuthStateListener() {
                  @Override
                  public void onAuthStateChanged(final AuthData data) {
                    emitter.onNext(data);
                  }
                };

            emitter.setCancellation(
                new AsyncEmitter.Cancellable() {
                  @Override
                  public void cancel() throws Exception {
                    firebase.removeAuthStateListener(listener);
                  }
                });

            firebase.addAuthStateListener(listener);
          }
        },
        mode);
  }

  public final Observable<Void> setValue(final Object value) {
    return setValue(value, null);
  }

  public final Observable<Void> setValue(final Object value, final Object priority) {
    return Observable.fromAsync(
        new Action1<AsyncEmitter<Void>>() {
          @Override
          public void call(final AsyncEmitter<Void> emitter) {
            firebase.setValue(
                value,
                priority,
                new Firebase.CompletionListener() {
                  @Override
                  public void onComplete(final FirebaseError error, final Firebase firebase) {
                    if (error == null) {
                      emitter.onCompleted();
                    } else {
                      emitter.onError(error.toException());
                    }
                  }
                });
          }
        },
        mode);
  }

  public final Observable<DataSnapshot> onValueEvent(final String path) {
    return Observable.fromAsync(
        new Action1<AsyncEmitter<DataSnapshot>>() {
          @Override
          public void call(final AsyncEmitter<DataSnapshot> emitter) {
            final ValueEventListener listener =
                new ValueEventListener() {
                  @Override
                  public void onDataChange(final DataSnapshot snapshot) {
                    emitter.onNext(snapshot);
                  }

                  @Override
                  public void onCancelled(final FirebaseError error) {
                    emitter.onError(error.toException());
                  }
                };

            emitter.setCancellation(
                new AsyncEmitter.Cancellable() {
                  @Override
                  public void cancel() throws Exception {
                    firebase.child(path).removeEventListener(listener);
                  }
                });

            firebase.child(path).addValueEventListener(listener);
          }
        },
        mode);
  }

  public Observable<DataSnapshot> onSingleValueEvent(final String path) {
    return Observable.fromAsync(
        new Action1<AsyncEmitter<DataSnapshot>>() {
          @Override
          public void call(final AsyncEmitter<DataSnapshot> emitter) {
            final ValueEventListener listener =
                new ValueEventListener() {
                  @Override
                  public void onDataChange(final DataSnapshot snapshot) {
                    emitter.onNext(snapshot);
                  }

                  @Override
                  public void onCancelled(final FirebaseError error) {
                    emitter.onError(error.toException());
                  }
                };

            emitter.setCancellation(
                new AsyncEmitter.Cancellable() {
                  @Override
                  public void cancel() throws Exception {
                    firebase.child(path).removeEventListener(listener);
                  }
                });

            firebase.child(path).addListenerForSingleValueEvent(listener);
          }
        },
        mode);
  }

  public final Observable<ChildEvent> onChildEvent(final String path) {
    return Observable.fromAsync(
        new Action1<AsyncEmitter<ChildEvent>>() {
          @Override
          public void call(final AsyncEmitter<ChildEvent> emitter) {
            final ChildEventListener listener =
                new ChildEventListener() {
                  @Override
                  public void onChildAdded(final DataSnapshot snapshot, final String previous) {
                    emitter.onNext(
                        ChildEvent.create(snapshot, Event.EventType.CHILD_ADDED, previous));
                  }

                  @Override
                  public void onChildChanged(final DataSnapshot snapshot, final String previous) {
                    emitter.onNext(
                        ChildEvent.create(snapshot, Event.EventType.CHILD_CHANGED, previous));
                  }

                  @Override
                  public void onChildRemoved(final DataSnapshot snapshot) {
                    emitter.onNext(ChildEvent.create(snapshot, Event.EventType.CHILD_REMOVED));
                  }

                  @Override
                  public void onChildMoved(final DataSnapshot snapshot, final String previous) {
                    emitter.onNext(
                        ChildEvent.create(snapshot, Event.EventType.CHILD_MOVED, previous));
                  }

                  @Override
                  public void onCancelled(final FirebaseError error) {
                    emitter.onError(error.toException());
                  }
                };

            emitter.setCancellation(
                new AsyncEmitter.Cancellable() {
                  @Override
                  public void cancel() throws Exception {
                    firebase.child(path).removeEventListener(listener);
                  }
                });

            firebase.child(path).addChildEventListener(listener);
          }
        },
        mode);
  }

  public final Observable<ChildEvent> onChildAdded(final String path) {
    return onChildEvent(path)
        .filter(
            new Func1<ChildEvent, Boolean>() {
              @Override
              public Boolean call(final ChildEvent childEvent) {
                return childEvent.getEventType().equals(Event.EventType.CHILD_ADDED);
              }
            });
  }

  public final Observable<ChildEvent> onChildChanged(final String path) {
    return onChildEvent(path)
        .filter(
            new Func1<ChildEvent, Boolean>() {
              @Override
              public Boolean call(final ChildEvent childEvent) {
                return childEvent.getEventType().equals(Event.EventType.CHILD_CHANGED);
              }
            });
  }

  public final Observable<ChildEvent> onChildRemoved(final String path) {
    return onChildEvent(path)
        .filter(
            new Func1<ChildEvent, Boolean>() {
              @Override
              public Boolean call(final ChildEvent childEvent) {
                return childEvent.getEventType().equals(Event.EventType.CHILD_REMOVED);
              }
            });
  }

  public final Observable<ChildEvent> onChildMoved(final String path) {
    return onChildEvent(path)
        .filter(
            new Func1<ChildEvent, Boolean>() {
              @Override
              public Boolean call(final ChildEvent childEvent) {
                return childEvent.getEventType().equals(Event.EventType.CHILD_MOVED);
              }
            });
  }
}
