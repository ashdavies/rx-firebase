package io.ashdavies.rx.rxfirebase;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.functions.Function;

class ChildEventClassTransformer<T> implements FlowableTransformer<ChildEvent, T> {

  private final Class<T> kls;

  ChildEventClassTransformer(Class<T> kls) {
    this.kls = kls;
  }

  @Override public Publisher<T> apply(Flowable<ChildEvent> upstream) {
    return upstream.map(new Mapper<>(kls));
  }

  private static class Mapper<T> implements Function<ChildEvent, T> {

    private final Class<T> kls;

    Mapper(Class<T> kls) {
      this.kls = kls;
    }

    @Override
    public T apply(ChildEvent event) throws Exception {
      return event.snapshot().getValue(kls);
    }
  }
}
