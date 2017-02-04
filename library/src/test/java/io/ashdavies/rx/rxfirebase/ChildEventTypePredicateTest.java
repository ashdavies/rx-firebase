package io.ashdavies.rx.rxfirebase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ChildEventTypePredicateTest {

  private ChildEventTypePredicate predicate;

  @Before
  public void setUp() throws Exception {
    predicate = new ChildEventTypePredicate(ChildEvent.Type.CHILD_ADDED);
  }

  @Test
  public void shouldAssertTypeEquals() throws Exception {
    assertThat(predicate.test(ChildEvent.create(null, ChildEvent.Type.CHILD_ADDED))).isTrue();
  }

  @Test
  public void shouldAssertTypeNotEquals() throws Exception {
    assertThat(predicate.test(ChildEvent.create(null, ChildEvent.Type.CHILD_REMOVED))).isFalse();
  }
}
