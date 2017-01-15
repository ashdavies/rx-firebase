package io.ashdavies.rx.rxfirebase;

import io.reactivex.functions.Function;

class ChildEventResolver<T> implements Function<ChildEvent, T> {

  private final SnapshotResolver<T> resolver;

  ChildEventResolver(SnapshotResolver<T> resolver) {
    this.resolver = resolver;
  }

  @Override
  public T apply(ChildEvent event) throws Exception {
    return resolver.resolve(event.snapshot());
  }
}
