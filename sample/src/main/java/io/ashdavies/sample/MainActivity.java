package io.ashdavies.sample;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import io.ashdavies.commons.activity.AbstractActivity;
import io.ashdavies.commons.adapter.AbstractAdapter;
import io.ashdavies.commons.adapter.DividerItemDecoration;
import io.ashdavies.commons.view.AbstractView;
import io.ashdavies.rx.rxfirebase.ChildEvent;
import io.ashdavies.rx.rxfirebase.RxFirebaseAuth;
import io.ashdavies.rx.rxfirebase.RxFirebaseDatabase;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import org.reactivestreams.Publisher;

public class MainActivity extends AbstractActivity {

  private static final String CUSTOMERS = "customers";

  private UserAdapter adapter;

  private Disposable disposable;
  private Unbinder unbinder;

  @BindView(R.id.recycler) RecyclerView recycler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    FirebaseApp.initializeApp(this);
    adapter = new UserAdapter(this);

    recycler.setAdapter(adapter);
    recycler.setLayoutManager(new LinearLayoutManager(this));
    recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

    disposable = RxFirebaseAuth.getInstance()
        .signInAnonymously()
        .flatMapPublisher(new Function<AuthResult, Publisher<UserEntity>>() {
          @Override
          public Publisher<UserEntity> apply(AuthResult result) throws Exception {
            return RxFirebaseDatabase.getInstance(CUSTOMERS)
                .onChildEventValue(ChildEvent.Type.CHILD_ADDED, UserEntity.class);
          }
        })
        .subscribe(new AdapterConsumer<>(adapter), new AbstractViewError(this));
  }

  @Override
  public void setContentView(@LayoutRes int layoutResId) {
    super.setContentView(layoutResId);
    unbinder = ButterKnife.bind(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    disposable.dispose();
    unbinder.unbind();
  }

  @Override
  protected int getLayoutId() {
    return R.layout.activity_main;
  }

  @Override
  public void onError(Throwable throwable) {
    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
  }

  private static class AdapterConsumer<T> implements Consumer<T> {

    private final AbstractAdapter<?, T> adapter;

    AdapterConsumer(AbstractAdapter<?, T> adapter) {
      this.adapter = adapter;
    }

    @Override
    public void accept(T item) throws Exception {
      adapter.addItem(item);
    }
  }

  private static class AbstractViewError implements Consumer<Throwable> {

    private final AbstractView view;

    AbstractViewError(AbstractView view) {
      this.view = view;
    }

    @Override
    public void accept(Throwable throwable) throws Exception {
      view.onError(throwable);
    }
  }
}
