package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.Query;

import io.reactivex.functions.Cancellable;

class ChildEventCancellable implements Cancellable {

  private final Query query;
  private final ChildEventListener listener;

  ChildEventCancellable(Query query, ChildEventListener listener) {
    this.query = query;
    this.listener = listener;
  }

  @Override
  public void cancel() throws Exception {
    query.removeEventListener(listener);
  }
}
