package com.quoctrungdhqn.shiportalandroid.main.presenter;

import com.quoctrungdhqn.shiportalandroid.base.BasePresenter;
import com.quoctrungdhqn.shiportalandroid.base.BaseView;
import com.quoctrungdhqn.shiportalandroid.data.response.UserResponse;

import java.util.List;

public interface MainControllerContract {

    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void showError(String message);

        void appendDataList(UserResponse userResponse, List<UserResponse.User> users, int nextPage, boolean isFinalPage);
    }

    interface Presenter extends BasePresenter {
        void getUserList(int page);
    }
}
