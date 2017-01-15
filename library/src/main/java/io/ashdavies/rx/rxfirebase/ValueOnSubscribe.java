package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.DatabaseReference;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

class ValueOnSubscribe implements CompletableOnSubscribe {

  private final DatabaseReference reference;

  private final Object value;
  private final Object priority;

  ValueOnSubscribe(DatabaseReference reference, Object value, Object priority) {
    this.reference = reference;

    this.value = value;
    this.priority = priority;
  }

  @Override
  public void subscribe(CompletableEmitter emitter) throws Exception {
    reference.setValue(value, priority, new ValueCompletionCompletable(emitter));
  }
}
