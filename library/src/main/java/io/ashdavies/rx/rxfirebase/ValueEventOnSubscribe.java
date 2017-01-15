package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.Query;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

class ValueEventOnSubscribe<T> implements FlowableOnSubscribe<T> {

  private final Query query;
  private final SnapshotResolver<T> resolver;

  ValueEventOnSubscribe(Query query, SnapshotResolver<T> resolver) {
    this.query = query;
    this.resolver = resolver;
  }

  @Override
  public void subscribe(FlowableEmitter<T> emitter) throws Exception {
    ValueEventFlowable<T> listener = new ValueEventFlowable<>(emitter, resolver);
    emitter.setCancellable(listener.cancellable(query));
    query.addValueEventListener(listener);
  }
}
