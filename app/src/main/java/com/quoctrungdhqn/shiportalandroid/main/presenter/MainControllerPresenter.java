package com.quoctrungdhqn.shiportalandroid.main.presenter;

import android.content.Context;

import com.quoctrungdhqn.shiportalandroid.data.RetrofitClient;
import com.quoctrungdhqn.shiportalandroid.data.response.UserResponse;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainControllerPresenter implements MainControllerContract.Presenter {
    private MainControllerContract.View mView;
    private CompositeDisposable compositeDisposable;
    private Context context;

    public MainControllerPresenter(MainControllerContract.View view, Context context) {
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
    public void getUserList(int page) {
        compositeDisposable.add(RetrofitClient.getServiceAPI(context).getUsers(page)
                .flatMap(userResponse -> {
                    if (userResponse.isSuccessful()) {
                        return Observable.just(userResponse.body());
                    } else {
                        return Observable.error(new Exception(userResponse.errorBody().string()));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResponse -> onGetUsersDataResponse(userResponse, page), this::onGetUsersError));
    }

    private void onGetUsersDataResponse(UserResponse userResponse, int page) {
        if (userResponse == null || userResponse.getUsers() == null) return;

        boolean isFinalPage = false;
        if (userResponse.getUsers().size() >= 9) {
            page++;
        } else {
            isFinalPage = true;
        }

        mView.appendDataList(userResponse, userResponse.getUsers(), page, isFinalPage);
    }

    private void onGetUsersError(Throwable throwable) {
        mView.showLoading();
        mView.showError(throwable.getMessage());
    }
}
