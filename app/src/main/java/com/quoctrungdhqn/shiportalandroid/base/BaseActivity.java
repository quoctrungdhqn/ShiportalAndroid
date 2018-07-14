package com.quoctrungdhqn.shiportalandroid.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

@SuppressLint("Registered")
public class BaseActivity extends RxAppCompatActivity implements LifecycleProvider<ActivityEvent> {
    private static final String TAG = "RxLifecycleAndroid";
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        lifecycleSubject.onNext(ActivityEvent.CREATE);

        // Specifically bind this until onPause()
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> Log.i(TAG, "Unsubscribing subscription from onCreate()"))
                .compose(this.bindUntilEvent(ActivityEvent.PAUSE))
                .subscribe(num -> Log.i(TAG, "Started in onCreate(), running until onPause(): " + num));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onStart() {
        lifecycleSubject.onNext(ActivityEvent.START);
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
        lifecycleSubject.onNext(ActivityEvent.RESUME);
        super.onResume();

        Log.d(TAG, "onResume()");
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> Log.i(TAG, "Unsubscribing subscription from onResume()"))
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(num -> Log.i(TAG, "Started in onResume(), running until in onDestroy(): " + num));
    }

    @Override
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }
}
