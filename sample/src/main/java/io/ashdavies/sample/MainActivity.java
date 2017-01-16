package io.ashdavies.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.ashdavies.rx.rxfirebase.RxFirebaseDatabase;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  protected void onResume() {
    super.onResume();
    init();
  }

  private void init() {
    RxFirebaseDatabase.getInstance().onChildAdded("articles")
        .subscribe();
  }
}
