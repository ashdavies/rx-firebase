package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.Query;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

class ChildEventOnSubscribe implements FlowableOnSubscribe<ChildEvent> {

  private final Query query;

  ChildEventOnSubscribe(Query query) {
    this.query = query;
  }

  @Override
  public void subscribe(FlowableEmitter<ChildEvent> emitter) throws Exception {
    FlowableChildEventListener listener = new FlowableChildEventListener(emitter);
    emitter.setCancellable(listener.cancellable(query));
    query.addChildEventListener(listener);
  }
}
