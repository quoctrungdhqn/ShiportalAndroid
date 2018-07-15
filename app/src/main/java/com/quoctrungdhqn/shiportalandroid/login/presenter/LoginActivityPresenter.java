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
    private CompositeDisposable mCompositeDisposable;
    private Context mContext;

    public LoginActivityPresenter(LoginActivityContract.View view, Context context) {
        mView = view;
        mContext = context;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
            mCompositeDisposable = null;
        }
    }

    @Override
    public void doLogin(LoginRequest loginRequest) {
        mView.showLoading();
        mCompositeDisposable.add(RetrofitClient.getServiceAPI(mContext).login(loginRequest)
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

                    // Save access_token & refresh_token for future
                    SharedPrefs.setStringPrefs(mContext, "access_token", response.getAccessToken());
                    SharedPrefs.setStringPrefs(mContext, "refresh_token", response.getRefreshToken());
                    mView.showLoginSuccess();
                }, throwable -> {
                    mView.hideLoading();
                    mView.showLoginError(throwable.getMessage());
                }));

    }
}
