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
    private CompositeDisposable mCompositeDisposable;
    private Context mContext;

    public MainControllerPresenter(MainControllerContract.View view, Context context) {
        mView = view;
        mCompositeDisposable = new CompositeDisposable();
        mContext = context;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        // Dispose
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
            mCompositeDisposable = null;
        }
    }

    @Override
    public void getUserList(int page) {
        mCompositeDisposable.add(RetrofitClient.getServiceAPI(mContext).getUsers(page)
                .flatMap(userResponse -> {
                    if (userResponse.isSuccessful()) {

                        List<UserResponse.User> users = userResponse.body().getUsers();
                        List<Observable<Response<UserDetailResponse>>> observableList = new ArrayList<>();

                        for (UserResponse.User item : users) {
                            observableList.add(RetrofitClient.getServiceAPI(mContext).getUserDetail(item.getUserId()));
                        }

                        /*Observable<UserResponse.User> observable = Observable.fromIterable(users);
                        observable.subscribe(new Observer<UserResponse.User>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(UserResponse.User user) {
                                observableList.add(RetrofitClient.getServiceAPI(mContext).getUserDetail(user.getUserId()));
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });*/

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
