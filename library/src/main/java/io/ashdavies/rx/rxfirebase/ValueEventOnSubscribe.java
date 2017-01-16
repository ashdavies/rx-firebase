package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

class ValueEventOnSubscribe implements FlowableOnSubscribe<DataSnapshot> {

  private final Query query;

  ValueEventOnSubscribe(Query query) {
    this.query = query;
  }

  @Override
  public void subscribe(FlowableEmitter<DataSnapshot> emitter) throws Exception {
    ValueEventFlowable listener = new ValueEventFlowable(emitter);
    emitter.setCancellable(listener.cancellable(query));
    query.addValueEventListener(listener);
  }
}
