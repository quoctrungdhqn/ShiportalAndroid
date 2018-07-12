package com.quoctrungdhqn.shiportalandroid.main.presenter;

import android.content.Context;

import com.quoctrungdhqn.shiportalandroid.data.RetrofitClient;
import com.quoctrungdhqn.shiportalandroid.data.response.UserResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MainActivityPresenter implements MainActivityContract.Presenter {
    private MainActivityContract.View mView;
    private CompositeDisposable compositeDisposable;
    private Context context;

    public MainActivityPresenter(MainActivityContract.View view, Context context) {
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResponse -> onGetUsersDataResponse(userResponse, page), this::onGetUsersError));
    }

    private void onGetUsersDataResponse(Response<UserResponse> userResponse, int page) {
        if (userResponse == null || userResponse.body() == null) return;
        UserResponse response = userResponse.body();
        if (response != null && response.getUsers() != null) {
            boolean isFinalPage = false;
            if (response.getUsers().size() >= 9) {
                page++;
            } else {
                isFinalPage = true;
            }

            mView.appendDataList(response, response.getUsers(), page, isFinalPage);
        }
    }

    private void onGetUsersError(Throwable throwable) {
    }
}
