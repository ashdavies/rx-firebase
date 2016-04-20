package com.chaos.rx;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.core.view.Event;
import com.firebase.client.snapshot.PriorityUtilities;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

public final class RxFirebase {
  private final Firebase firebase;

  private RxFirebase(Firebase firebase) {
    this.firebase = firebase;
  }

  public static RxFirebase with(final Firebase firebase) {
    return new RxFirebase(firebase);
  }

  public final Observable<AuthData> authAnonymously() {
    return Observable.create(new Observable.OnSubscribe<AuthData>() {
      @Override public void call(final Subscriber<? super AuthData> subscriber) {
        firebase.authAnonymously(new Firebase.AuthResultHandler() {
          @Override public void onAuthenticated(final AuthData authData) {
            subscriber.onNext(authData);
            subscriber.onCompleted();
          }

          @Override public void onAuthenticationError(final FirebaseError firebaseError) {
            subscriber.onError(firebaseError.toException());
          }
        });
      }
    });
  }

  public final Observable<AuthData> authWithPassword(final String email, final String password) {
    return Observable.create(new Observable.OnSubscribe<AuthData>() {
      @Override public void call(final Subscriber<? super AuthData> subscriber) {
        firebase.authWithPassword(email, password, new Firebase.AuthResultHandler() {
          @Override public void onAuthenticated(final AuthData authData) {
            subscriber.onNext(authData);
            subscriber.onCompleted();
          }

          @Override public void onAuthenticationError(final FirebaseError firebaseError) {
            subscriber.onError(firebaseError.toException());
          }
        });
      }
    });
  }

  public final Observable<AuthData> authWithOAuthToken(final String token, final String provider) {
    return Observable.create(new Observable.OnSubscribe<AuthData>() {
      @Override public void call(final Subscriber<? super AuthData> subscriber) {
        firebase.authWithOAuthToken(provider, token, new Firebase.AuthResultHandler() {
          @Override public void onAuthenticated(final AuthData authData) {
            subscriber.onNext(authData);
            subscriber.onCompleted();
          }

          @Override public void onAuthenticationError(final FirebaseError firebaseError) {
            subscriber.onError(firebaseError.toException());
          }
        });
      }
    });
  }

  public final Observable<Map<String, Object>> createUser(final String email, final String password) {
    return Observable.create(new Observable.OnSubscribe<Map<String, Object>>() {
      @Override public void call(final Subscriber<? super Map<String, Object>> subscriber) {
        firebase.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
          @Override public void onSuccess(Map<String, Object> result) {
            subscriber.onNext(result);
            subscriber.onCompleted();
          }

          @Override public void onError(FirebaseError firebaseError) {
            subscriber.onError(firebaseError.toException());
          }
        });
      }
    });
  }

  public final Observable<Boolean> setValue(final Object value) {
    return setValue(value, PriorityUtilities.parsePriority(null));
  }

  public final Observable<Boolean> setValue(final Object value, final Object priority) {
    return Observable.create(new Observable.OnSubscribe<Boolean>() {
      @Override public void call(final Subscriber<? super Boolean> subscriber) {
        firebase.setValue(value, priority, new Firebase.CompletionListener() {
          @Override public void onComplete(final FirebaseError firebaseError, Firebase firebase) {
            if (firebaseError == null) {
              subscriber.onNext(true);
              subscriber.onCompleted();
            } else {
              subscriber.onError(firebaseError.toException());
            }
          }
        });
      }
    });
  }

  public final Observable<AuthData> onAuthStateEvent() {
    return Observable.create(new Observable.OnSubscribe<AuthData>() {
      @Override public void call(final Subscriber<? super AuthData> subscriber) {
        final Firebase.AuthStateListener listener = new Firebase.AuthStateListener() {
          @Override public void onAuthStateChanged(final AuthData authData) {
            subscriber.onNext(authData);
          }
        };

        subscriber.add(Subscriptions.create(new Action0() {
          @Override public void call() {
            firebase.removeAuthStateListener(listener);
          }
        }));

        firebase.addAuthStateListener(listener);
      }
    });
  }

  public final Observable<DataSnapshot> onValueEvent(final String path) {
    return Observable.create(new Observable.OnSubscribe<DataSnapshot>() {
      @Override public void call(final Subscriber<? super DataSnapshot> subscriber) {
        final ValueEventListener valueEventListener = firebase.child(path).addValueEventListener(new ValueEventListener() {
          @Override public void onDataChange(final DataSnapshot dataSnapshot) {
            subscriber.onNext(dataSnapshot);
          }

          @Override public void onCancelled(final FirebaseError firebaseError) {
            subscriber.onError(firebaseError.toException());
          }
        });

        subscriber.add(Subscriptions.create(new Action0() {

          @Override
          public void call() {
            firebase.child(path).removeEventListener(valueEventListener);
          }
        }));
      }
    });
  }

  public Observable<DataSnapshot> onSingleValueEvent(final String path) {
    return Observable.create(new Observable.OnSubscribe<DataSnapshot>() {
      @Override public void call(final Subscriber<? super DataSnapshot> subscriber) {
        final ValueEventListener valueEventListener = new ValueEventListener() {
          @Override public void onDataChange(final DataSnapshot dataSnapshot) {
            subscriber.onNext(dataSnapshot);
          }

          @Override public void onCancelled(final FirebaseError firebaseError) {
            subscriber.onError(firebaseError.toException());
          }
        };

        firebase.child(path).addListenerForSingleValueEvent(valueEventListener);

        subscriber.add(Subscriptions.create(new Action0() {
          @Override public void call() {
            firebase.child(path).removeEventListener(valueEventListener);
          }
        }));
      }
    });
  }

  public final Observable<ChildEvent> onChildEvent(final String path) {
    return Observable.create(new Observable.OnSubscribe<ChildEvent>() {
      @Override public void call(final Subscriber<? super ChildEvent> subscriber) {
        firebase.child(path).addChildEventListener(new ChildEventListener() {
          @Override public void onChildAdded(final DataSnapshot dataSnapshot, final String previousChildName) {
            subscriber.onNext(ChildEvent.create(dataSnapshot, Event.EventType.CHILD_ADDED, previousChildName));
          }

          @Override public void onChildChanged(final DataSnapshot dataSnapshot, final String previousChildName) {
            subscriber.onNext(ChildEvent.create(dataSnapshot, Event.EventType.CHILD_CHANGED, previousChildName));
          }

          @Override public void onChildRemoved(final DataSnapshot dataSnapshot) {
            subscriber.onNext(ChildEvent.create(dataSnapshot, Event.EventType.CHILD_REMOVED));
          }

          @Override public void onChildMoved(final DataSnapshot dataSnapshot, final String previousChildName) {
            subscriber.onNext(ChildEvent.create(dataSnapshot, Event.EventType.CHILD_MOVED, previousChildName));
          }

          @Override public void onCancelled(final FirebaseError firebaseError) {
            subscriber.onError(firebaseError.toException());
          }
        });
      }
    });
  }

  public final Observable<ChildEvent> onChildAdded(final String path) {
    return onChildEvent(path).filter(new Func1<ChildEvent, Boolean>() {
      @Override public Boolean call(final ChildEvent childEvent) {
        return childEvent.getEventType().equals(Event.EventType.CHILD_ADDED);
      }
    });
  }

  public final Observable<ChildEvent> onChildChanged(final String path) {
    return onChildEvent(path).filter(new Func1<ChildEvent, Boolean>() {
      @Override public Boolean call(final ChildEvent childEvent) {
        return childEvent.getEventType().equals(Event.EventType.CHILD_CHANGED);
      }
    });
  }

  public final Observable<ChildEvent> onChildRemoved(final String path) {
    return onChildEvent(path).filter(new Func1<ChildEvent, Boolean>() {
      @Override public Boolean call(final ChildEvent childEvent) {
        return childEvent.getEventType().equals(Event.EventType.CHILD_REMOVED);
      }
    });
  }

  public final Observable<ChildEvent> onChildMoved(final String path) {
    return onChildEvent(path).filter(new Func1<ChildEvent, Boolean>() {
      @Override public Boolean call(final ChildEvent childEvent) {
        return childEvent.getEventType().equals(Event.EventType.CHILD_MOVED);
      }
    });
  }
}
