package io.ashdavies.rx.rxfirebase

import com.google.firebase.database.DataSnapshot

data class ChildEvent(val snapshot: DataSnapshot, val type: Type, val previous: String?) {

  constructor(snapshot: DataSnapshot, type: Type) : this(snapshot, type, null)

  enum class Type {
    CHILD_ADDED,
    CHILD_CHANGED,
    CHILD_REMOVED,
    CHILD_MOVED
  }
}
