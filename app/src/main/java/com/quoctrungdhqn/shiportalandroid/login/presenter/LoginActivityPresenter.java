package com.quoctrungdhqn.shiportalandroid.login.presenter;

import android.content.Context;

import com.quoctrungdhqn.shiportalandroid.data.RetrofitClient;
import com.quoctrungdhqn.shiportalandroid.data.request.LoginRequest;
import com.quoctrungdhqn.shiportalandroid.data.response.LoginResponse;
import com.quoctrungdhqn.shiportalandroid.utils.SharedPrefs;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class LoginActivityPresenter implements LoginActivityContract.Presenter {
    private LoginActivityContract.View mView;
    private CompositeDisposable compositeDisposable;
    private Context context;

    public LoginActivityPresenter(LoginActivityContract.View view, Context context) {
        mView = view;
        compositeDisposable = new CompositeDisposable();
        this.context = context;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
    }

    @Override
    public void doLogin(LoginRequest loginRequest) {
        mView.showLoading();
        compositeDisposable.add(RetrofitClient.getServiceAPI(context).login(loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::loginResponse, this::loginError));

    }

    private void loginResponse(Response<LoginResponse> loginResponse) {
        mView.hideLoading();
        if (loginResponse == null || loginResponse.body() == null) return;
        SharedPrefs.setStringPrefs(context, "access_token", Objects.requireNonNull(loginResponse.body()).getAccessToken());
        SharedPrefs.setStringPrefs(context, "refresh_token", Objects.requireNonNull(loginResponse.body()).getRefreshToken());
        SharedPrefs.setStringPrefs(context, "token_type", Objects.requireNonNull(loginResponse.body()).getTokenType());
        mView.showLoginSuccess();
    }

    private void loginError(Throwable throwable) {
        mView.hideLoading();
        mView.showLoginError(throwable.getMessage());
    }
}
