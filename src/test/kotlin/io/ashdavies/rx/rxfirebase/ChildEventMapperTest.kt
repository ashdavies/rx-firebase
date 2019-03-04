package io.ashdavies.rx.rxfirebase

import com.google.common.truth.Truth.assertThat
import com.google.firebase.database.DataSnapshot
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import io.ashdavies.rx.rxfirebase.ChildEvent.Type.CHILD_ADDED
import org.junit.jupiter.api.Test

internal class ChildEventMapperTest {

  private val snapshot: DataSnapshot = mock()

  private val mapper: ChildEventMapper<String> = ChildEventMapper(String::class.java)

  @Test
  fun `should return snapshot value`() {
    given(snapshot.getValue(String::class.java)).willReturn("Hello World")

    assertThat(mapper.apply(ChildEvent(snapshot, CHILD_ADDED))).isEqualTo("Hello World")
  }
}
