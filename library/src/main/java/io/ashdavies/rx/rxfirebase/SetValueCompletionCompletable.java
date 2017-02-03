package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import io.reactivex.CompletableEmitter;

class SetValueCompletionCompletable implements DatabaseReference.CompletionListener {

  private final CompletableEmitter emitter;

  SetValueCompletionCompletable(CompletableEmitter emitter) {
    this.emitter = emitter;
  }

  @Override
  public void onComplete(DatabaseError error, DatabaseReference reference) {
    if (emitter.isCancelled()) {
      return;
    }

    if (error != null) {
      emitter.onError(error.toException());
      return;
    }

    emitter.onComplete();
  }
}
