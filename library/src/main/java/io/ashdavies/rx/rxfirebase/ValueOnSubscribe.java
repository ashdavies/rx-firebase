package io.ashdavies.rx.rxfirebase;

import android.support.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;

import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;

class ValueOnSubscribe implements CompletableOnSubscribe {

  private final DatabaseReference reference;
  private final Object value;

  @Nullable
  private final Object priority;

  ValueOnSubscribe(DatabaseReference reference, Object value) {
    this(reference, value, null);
  }

  ValueOnSubscribe(DatabaseReference reference, Object value, @Nullable Object priority) {
    this.reference = reference;
    this.value = value;

    this.priority = priority;
  }

  @Override
  public void subscribe(CompletableEmitter emitter) throws Exception {
    reference.setValue(value, priority, new ValueCompletionCompletable(emitter));
  }
}
