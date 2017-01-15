package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.functions.Cancellable;

class ValueEventCancellable implements Cancellable {

  private final Query query;
  private final ValueEventListener listener;

  ValueEventCancellable(Query query, ValueEventListener listener) {
    this.query = query;
    this.listener = listener;
  }

  @Override
  public void cancel() throws Exception {
    query.removeEventListener(listener);
  }
}
