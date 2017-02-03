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
import io.ashdavies.rx.rxfirebase.RxFirebaseDatabase;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AbstractActivity {

  private static final String CUSTOMERS = "customers";
  private static final String USERS = "users";

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

    disposable = RxFirebaseDatabase.getInstance(USERS)
        .onChildAdded()
        .map(childEvent -> childEvent.snapshot().getValue(UserEntity.class))
        //.doOnNext(this::createCustomerEntry)
        .subscribe(adapter::addItem, this::onError);
  }

  @Override
  public void setContentView(@LayoutRes int layoutResId) {
    super.setContentView(layoutResId);
    unbinder = ButterKnife.bind(this);
  }

  private void createCustomerEntry(UserEntity user) {
    RxFirebaseDatabase.getInstance()
        .setValue(user)
        .subscribe(() -> {
        }, this::onError);
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
