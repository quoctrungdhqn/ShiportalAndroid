package com.quoctrungdhqn.shiportalandroid.login.presenter;

import android.content.Context;

import com.quoctrungdhqn.shiportalandroid.data.RetrofitClient;
import com.quoctrungdhqn.shiportalandroid.data.request.LoginRequest;
import com.quoctrungdhqn.shiportalandroid.utils.SharedPrefs;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivityPresenter implements LoginActivityContract.Presenter {
    private LoginActivityContract.View mView;
    private CompositeDisposable compositeDisposable;
    private Context context;

    public LoginActivityPresenter(LoginActivityContract.View view, Context context) {
        mView = view;
        this.context = context;
        compositeDisposable = new CompositeDisposable();
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
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return Observable.just(response.body());
                    } else {
                        return Observable.error(new Exception(response.errorBody().string()));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    mView.hideLoading();
                    if (response == null) return;
                    SharedPrefs.setStringPrefs(context, "access_token", response.getAccessToken());
                    SharedPrefs.setStringPrefs(context, "refresh_token", response.getRefreshToken());
                    mView.showLoginSuccess();
                }, throwable -> {
                    mView.hideLoading();
                    mView.showLoginError(throwable.getMessage());
                }));

    }
}
