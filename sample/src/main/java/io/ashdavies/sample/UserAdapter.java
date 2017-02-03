package io.ashdavies.sample;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.ashdavies.commons.adapter.AbstractAdapter;

class UserAdapter extends AbstractAdapter<UserAdapter.Holder, UserEntity> {

  UserAdapter(Context context) {
    super(context);
  }

  @Override
  public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new Holder(parent.getContext(), getInflater().inflate(R.layout.item_user, parent, false));
  }

  class Holder extends AbstractAdapter.ViewHolder<UserEntity> {

    @BindView(R.id.name) TextView name;
    @BindView(R.id.company) TextView company;
    @BindView(R.id.email) TextView email;

    Holder(Context context, View view) {
      super(context, view);
      ButterKnife.bind(this, view);
    }

    @Override
    protected void bind(UserEntity user) {
      name.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
      company.setText(user.getCompanyName());
      email.setText(user.getEmail());
    }
  }
}
