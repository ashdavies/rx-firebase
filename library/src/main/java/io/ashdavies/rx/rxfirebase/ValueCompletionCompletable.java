package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import io.reactivex.CompletableEmitter;

class ValueCompletionCompletable implements DatabaseReference.CompletionListener {

  private final CompletableEmitter emitter;

  ValueCompletionCompletable(CompletableEmitter emitter) {
    this.emitter = emitter;
  }

  @Override
  public void onComplete(DatabaseError error, DatabaseReference reference) {
    if (error != null) {
      onError(error);
      return;
    }

    emitter.onComplete();
  }

  private void onError(DatabaseError error) {
    emitter.onError(error.toException());
  }
}
