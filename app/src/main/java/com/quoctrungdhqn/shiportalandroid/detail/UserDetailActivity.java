package com.quoctrungdhqn.shiportalandroid.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quoctrungdhqn.shiportalandroid.R;
import com.quoctrungdhqn.shiportalandroid.base.BaseActivity;
import com.quoctrungdhqn.shiportalandroid.data.response.UserDetailResponse;
import com.quoctrungdhqn.shiportalandroid.utils.Utils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UserDetailActivity extends BaseActivity {
    public static final String EXTRA_NAME = "user_extra";
    private Unbinder unbinder;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.tv_full_name)
    TextView tvFullName;

    @BindView(R.id.tv_mobile_phone)
    TextView tvMobilePhone;

    @BindView(R.id.tv_email)
    TextView tvEmail;

    @BindView(R.id.tv_description)
    TextView tvDescription;

    @BindView(R.id.imageAvatar)
    ImageView imageAvatar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        unbinder = ButterKnife.bind(this);

        // init toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get data from intent
        Intent intent = getIntent();
        UserDetailResponse user = intent.getParcelableExtra(EXTRA_NAME);

        String name = String.format(Locale.getDefault(), "%s %s", user.getBusinessName(), user.getLastName());
        collapsingToolbarLayout.setTitle(name);

        tvFullName.setText(name);
        tvMobilePhone.setText(TextUtils.isEmpty(user.getMobile()) ? "" : user.getMobile());
        tvEmail.setText(TextUtils.isEmpty(user.getEmail()) ? "" : user.getEmail());
        tvDescription.setText(TextUtils.isEmpty(user.getDescription()) ? "" : user.getDescription());
        String profile_pic_url = "https://secure.gravatar.com/avatar/" + Utils.md5Hex(user.getEmail());
        Glide.with(this).load(profile_pic_url).into(imageAvatar);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
