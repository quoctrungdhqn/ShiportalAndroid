package com.quoctrungdhqn.shiportalandroid.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.EditText;

import com.quoctrungdhqn.shiportalandroid.R;
import com.quoctrungdhqn.shiportalandroid.base.BaseActivity;
import com.quoctrungdhqn.shiportalandroid.data.request.LoginRequest;
import com.quoctrungdhqn.shiportalandroid.login.presenter.LoginActivityContract;
import com.quoctrungdhqn.shiportalandroid.login.presenter.LoginActivityPresenter;
import com.quoctrungdhqn.shiportalandroid.main.MainActivity;
import com.quoctrungdhqn.shiportalandroid.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginActivity extends BaseActivity implements LoginActivityContract.View {
    private Unbinder unbinder;

    @BindView(R.id.et_username)
    EditText etUsername;

    @BindView(R.id.et_password)
    EditText etPassword;

    private LoginActivityContract.Presenter mPresenter;
    private AlertDialog mDialog;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        if (savedInstanceState != null) {
            etUsername.setText(savedInstanceState.getString("USERNAME"));
            etPassword.setText(savedInstanceState.getString("PASSWORD"));
        }
    }

    private void initViews() {
        unbinder = ButterKnife.bind(this);
        mDialog = Utils.getLoadingDialog(this);
        mPresenter = new LoginActivityPresenter(this, this);
    }

    @Override
    public void setPresenter(LoginActivityContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @OnClick(R.id.btn_login)
    public void onNextActivity() {
        if (!validateForm()) return;
        mPresenter.doLogin(new LoginRequest("1", etUsername.getText().toString(),
                etPassword.getText().toString(), "password"));
    }

    @Override
    public void showLoginSuccess() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void showLoginError(String message) {
        Utils.showBasicDialog(this, null, message, (dialog, which) -> dialog.dismiss());
    }

    private boolean validateForm() {
        boolean valid = true;
        String username = etUsername.getText().toString();
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Your username can not be empty.");
            valid = false;
        } else {
            etUsername.setError(null);
        }

        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Your password can not be empty.");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }

    @Override
    public void showLoading() {
        mDialog.show();
    }

    @Override
    public void hideLoading() {
        mDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("USERNAME", etUsername.getText().toString());
        outState.putString("PASSWORD", etPassword.getText().toString());
    }

}
