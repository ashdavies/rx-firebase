package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.Query;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

class SingleValueEventOnSubscribe<T> implements SingleOnSubscribe<T> {

  private final Query query;
  private final SnapshotResolver<T> resolver;

  SingleValueEventOnSubscribe(Query query, SnapshotResolver<T> resolver) {
    this.query = query;
    this.resolver = resolver;
  }

  @Override
  public void subscribe(SingleEmitter<T> emitter) throws Exception {
    ValueEventSingle<T> listener = new ValueEventSingle<>(emitter, resolver);
    emitter.setCancellable(listener.cancellable(query));
    query.addListenerForSingleValueEvent(listener);
  }
}
