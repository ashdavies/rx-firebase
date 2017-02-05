package io.ashdavies.rx.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NullableMaybeOnSubscribeTest {

  private static final String GINGERNUTS = "GINGERNUTS";

  @Mock MaybeEmitter<String> emitter;

  @Test
  public void shouldNotEmitNullOnSuccess() throws Exception {
    MaybeOnSubscribe<String> onSubscribe = new NullableMaybeOnSubscribe<>(null);

    onSubscribe.subscribe(emitter);

    verify(emitter, never()).onSuccess(anyString());
    verify(emitter).onComplete();
  }

  @Test
  public void shouldEmitOnSuccess() throws Exception {
    MaybeOnSubscribe<String> onSubscribe = new NullableMaybeOnSubscribe<>(GINGERNUTS);

    onSubscribe.subscribe(emitter);

    verify(emitter).onSuccess(GINGERNUTS);
    verify(emitter).onComplete();
  }
}
