package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

class SingleValueEventOnSubscribe implements SingleOnSubscribe<DataSnapshot> {

  private final Query query;

  SingleValueEventOnSubscribe(Query query) {
    this.query = query;
  }

  @Override
  public void subscribe(SingleEmitter<DataSnapshot> emitter) throws Exception {
    ValueEventSingle listener = new ValueEventSingle(emitter);
    emitter.setCancellable(listener.cancellable(query));
    query.addListenerForSingleValueEvent(listener);
  }
}
