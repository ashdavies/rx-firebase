package io.ashdavies.rx.rxfirebase

import io.reactivex.Maybe
import io.reactivex.MaybeSource
import io.reactivex.functions.Function

internal class ChildEventMapper<T>(private val kls: Class<T>) : Function<ChildEvent, MaybeSource<T>> {

  override fun apply(event: ChildEvent): MaybeSource<T> = Maybe.fromCallable<T> {
    event
        .snapshot
        .getValue(kls)
  }
}
