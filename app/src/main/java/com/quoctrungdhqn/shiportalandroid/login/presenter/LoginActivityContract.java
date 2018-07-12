package com.quoctrungdhqn.shiportalandroid.login.presenter;

import com.quoctrungdhqn.shiportalandroid.base.BasePresenter;
import com.quoctrungdhqn.shiportalandroid.base.BaseView;
import com.quoctrungdhqn.shiportalandroid.data.request.LoginRequest;

public interface LoginActivityContract {

    interface View extends BaseView<Presenter> {
        void showLoading();

        void hideLoading();

        void showLoginSuccess();

        void showLoginError(String message);
    }

    interface Presenter extends BasePresenter {
        void doLogin(LoginRequest loginRequest);
    }
}
