package io.ashdavies.rx.rxfirebase;

import io.reactivex.functions.Cancellable;

interface TypeCancellable<T> {

  Cancellable cancellable(T t);
}
