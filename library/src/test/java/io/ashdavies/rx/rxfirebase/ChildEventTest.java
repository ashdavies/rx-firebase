package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.DataSnapshot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ChildEventTest {

  private static final String PREVIOUS = "previous";

  @Mock DataSnapshot snapshot;

  @Test
  public void shouldCreateChildEvent() throws Exception {
    ChildEvent event = ChildEvent.create(snapshot, ChildEvent.Type.CHILD_ADDED, PREVIOUS);

    assertThat(event.snapshot()).isEqualTo(snapshot);
    assertThat(event.type()).isEqualTo(ChildEvent.Type.CHILD_ADDED);
    assertThat(event.previous()).isEqualTo(PREVIOUS);
  }

  @Test
  public void shouldCreateWithNullPrevious() throws Exception {
    ChildEvent event = ChildEvent.create(snapshot, ChildEvent.Type.CHILD_ADDED);

    assertThat(event.snapshot()).isEqualTo(snapshot);
    assertThat(event.type()).isEqualTo(ChildEvent.Type.CHILD_ADDED);
    assertThat(event.previous()).isNull();
  }
}
