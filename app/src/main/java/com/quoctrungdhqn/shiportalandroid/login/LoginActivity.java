package com.quoctrungdhqn.shiportalandroid.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.quoctrungdhqn.shiportalandroid.R;
import com.quoctrungdhqn.shiportalandroid.data.request.LoginRequest;
import com.quoctrungdhqn.shiportalandroid.login.presenter.LoginActivityContract;
import com.quoctrungdhqn.shiportalandroid.login.presenter.LoginActivityPresenter;
import com.quoctrungdhqn.shiportalandroid.main.MainActivity;
import com.quoctrungdhqn.shiportalandroid.utils.Utils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

public class LoginActivity extends RxAppCompatActivity implements LoginActivityContract.View {
    private static final String TAG = "LoginActivity";
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
        Log.d(TAG, "onCreate()");

        setContentView(R.layout.activity_login);
        initViews();
        mPresenter = new LoginActivityPresenter(this, this);

        // Specifically bind this until onPause()
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> Log.i(TAG, "Unsubscribing subscription from onCreate()"))
                .compose(this.bindUntilEvent(ActivityEvent.PAUSE))
                .subscribe(num -> Log.i(TAG, "Started in onCreate(), running until onPause(): " + num));
    }

    private void initViews() {
        ButterKnife.bind(this);
        mDialog = Utils.getLoadingDialog(this);
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

    @SuppressLint("CheckResult")
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");

        // Using automatic unsubscription, this should determine that the correct time to
        // unsubscribe is onStop (the opposite of onStart).
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> Log.i(TAG, "Unsubscribing subscription from onStart()"))
                .compose(this.bindToLifecycle())
                .subscribe(num -> Log.i(TAG, "Started in onStart(), running until in onStop(): " + num));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");

        // `this.<Long>` is necessary if you're compiling on JDK7 or below.
        //
        // If you're using JDK8+, then you can safely remove it.
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> Log.i(TAG, "Unsubscribing subscription from onResume()"))
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(num -> Log.i(TAG, "Started in onResume(), running until in onDestroy(): " + num));
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy()");
    }
}
