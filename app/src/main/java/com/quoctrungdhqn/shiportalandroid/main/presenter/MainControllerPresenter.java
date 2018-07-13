package com.quoctrungdhqn.shiportalandroid.main.presenter;

import android.content.Context;

import com.quoctrungdhqn.shiportalandroid.data.RetrofitClient;
import com.quoctrungdhqn.shiportalandroid.data.response.UserDetailResponse;
import com.quoctrungdhqn.shiportalandroid.data.response.UserResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

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

                        List<UserResponse.User> users = userResponse.body().getUsers();
                        List<Observable<Response<UserDetailResponse>>> observableList = new ArrayList<>();

                        for (UserResponse.User item : users) {
                            observableList.add(RetrofitClient.getServiceAPI(context).getUserDetail(item.getUserId()));
                        }

                        return Observable.combineLatest(observableList, objects -> objects);
                    } else {
                        return Observable.error(new Exception(userResponse.errorBody().string()));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResponse -> onGetUsersDataResponse(userResponse, page), this::onGetUsersError));
    }

    private void onGetUsersDataResponse(Object[] userResponses, int page) {
        List<UserDetailResponse> users = new ArrayList<>();
        for (Object item : userResponses) {
            Response<UserDetailResponse> response = (Response<UserDetailResponse>) item;
            users.add(response.body());
        }

        boolean isFinalPage = false;
        if (users.size() >= 9) {
            page++;
        } else {
            isFinalPage = true;
        }

        mView.appendDataList(users, page, isFinalPage);
    }

    private void onGetUsersError(Throwable throwable) {
        mView.hideLoading();
        mView.showError(throwable.getMessage());
    }
}
