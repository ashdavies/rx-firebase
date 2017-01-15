package io.ashdavies.rx.rxfirebase;

import io.reactivex.functions.Predicate;

class ChildEventTypePredicate implements Predicate<ChildEvent> {

  private final ChildEvent.Type type;

  ChildEventTypePredicate(ChildEvent.Type type) {
    this.type = type;
  }

  @Override
  public boolean test(ChildEvent event) throws Exception {
    return event.type().equals(type);
  }
}
