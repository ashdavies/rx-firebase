package io.ashdavies.sample;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.ashdavies.commons.activity.AbstractActivity;
import io.ashdavies.commons.adapter.DividerItemDecoration;
import io.ashdavies.rx.rxfirebase.ChildEvent;
import io.ashdavies.rx.rxfirebase.RxFirebaseAuth;
import io.ashdavies.rx.rxfirebase.RxFirebaseDatabase;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

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
        .flatMapPublisher(result -> getUserStream())
        .subscribe(adapter::addItem, this::onError);
  }

  private Flowable<UserEntity> getUserStream() {
    return RxFirebaseDatabase.getInstance(CUSTOMERS)
        .onChildEventValue(ChildEvent.Type.CHILD_ADDED, UserEntity.class);
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
}
