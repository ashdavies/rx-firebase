package io.ashdavies.rx.rxfirebase;

import com.google.firebase.database.DataSnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ChildEventClassTransformerTest {

  private static final String EVENT = "event";

  private FlowableTransformer<ChildEvent, String> transformer;

  @Mock DataSnapshot snapshot;

  @Before
  public void setUp() throws Exception {
    transformer = new ChildEventClassTransformer<>(String.class);
  }

  @Test
  public void shouldTransformChildEvent() throws Exception {
    ChildEvent event = ChildEvent.create(snapshot, ChildEvent.Type.CHILD_ADDED);
    given(snapshot.getValue(String.class)).willReturn(EVENT);

    Flowable.just(event)
        .compose(transformer)
        .test()
        .assertValue(EVENT)
        .assertComplete();
  }
}
