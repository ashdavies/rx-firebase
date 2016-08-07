package io.ashdavies.rxfirebase;

import com.firebase.client.Firebase;

public final class RxFirebaseWrapper {
  private static RxFirebase instance;
  private static Firebase firebase;

  public static void setUrl(String url) {
    setFirebase(new Firebase(url));
  }

  public static void setFirebase(Firebase firebase) {
    RxFirebaseWrapper.firebase = firebase;
  }

  public static synchronized RxFirebase getInstance() {
    if (instance == null) {
      instance = buildInstance();
    }

    return instance;
  }

  private static RxFirebase buildInstance() {
    if (firebase == null) {
      throw new NullPointerException("Cannot wrap null Firebase reference");
    }

    return RxFirebase.with(firebase);
  }
}
