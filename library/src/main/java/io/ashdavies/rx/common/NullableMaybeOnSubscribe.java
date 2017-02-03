package io.ashdavies.rx.common;

import javax.annotation.Nullable;

import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;

public class NullableMaybeOnSubscribe<T> implements MaybeOnSubscribe<T> {

  @Nullable
  private final T value;

  public NullableMaybeOnSubscribe(@Nullable T value) {
    this.value = value;
  }

  @Override
  public void subscribe(MaybeEmitter<T> emitter) throws Exception {
    if (value != null) {
      emitter.onSuccess(value);
    }

    emitter.onComplete();
  }
}
