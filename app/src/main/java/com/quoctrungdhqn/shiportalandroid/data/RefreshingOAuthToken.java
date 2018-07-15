package com.quoctrungdhqn.shiportalandroid.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.quoctrungdhqn.shiportalandroid.data.request.RefreshTokenRequest;
import com.quoctrungdhqn.shiportalandroid.utils.SharedPrefs;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RefreshingOAuthToken implements Interceptor {
    private static final String TAG = "RefreshingOAuthToken";
    private boolean isRefresh = false;
    private String refreshToken = null;
    private Context context;
    private CompositeDisposable mCompositeDisposable;
    private String accessToken;

    RefreshingOAuthToken(Context context) {
        this.context = context;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        refreshToken = SharedPrefs.getStringPrefs(context, "refresh_token");

        // Build new request
        Request.Builder builder = request.newBuilder();
        builder.header("Accept", "application/json");
        builder.header("Authorization", "Bearer ");

        accessToken = SharedPrefs.getStringPrefs(context, "access_token"); // Save token of this request for future
        setAuthHeader(builder, accessToken); // Write current token to request

        request = builder.build(); // Overwrite old request
        Response response = chain.proceed(request); // Perform request, here original request will be executed

        // The access token provided has expired
        if (response.code() == 401) {
            synchronized (this) {
                String currentToken = SharedPrefs.getStringPrefs(context, "access_token");
                Log.d("Token old", currentToken);
                if (currentToken != null) {
                    fetchToken();

                    if (accessToken != null) { // Retry requires new auth token
                        Log.d("Token new", accessToken);
                        setAuthHeader(builder, accessToken);  // Set auth token to updated
                        request = builder.build();
                        return chain.proceed(request); // Repeat request with new token
                    }
                }

            }
        }

        return response;
    }

    private void setAuthHeader(Request.Builder builder, String token) {
        if (token != null) { // Add Auth token to each request if authorized
            builder.header("Authorization", String.format("Bearer %s", token));
        }
    }

    private void fetchToken() {
        // Refresh token, synchronously, save it, and return result code
        if (!isRefresh) {
            isRefresh = true;
            if (refreshToken != null) {
                RefreshTokenRequest tokenRequest = new RefreshTokenRequest(
                        "1",
                        "api", "password",
                        "refresh_token",
                        refreshToken);
                try {
                    isRefresh = false;
                    mCompositeDisposable.add(RetrofitClient.getServiceAPI(context).refreshToken(tokenRequest)
                            .flatMap(response -> {
                                if (response.isSuccessful()) {
                                    return Single.just(response.body());
                                } else {
                                    return Single.error(new Exception(response.errorBody().string()));
                                }
                            })
                            .subscribe(response -> {
                                accessToken = response.getAccessToken();
                                Log.d("Token api", accessToken);
                                SharedPrefs.setStringPrefs(context, "access_token", response.getAccessToken());

                                // Dispose
                                if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
                                    mCompositeDisposable.dispose();
                                    mCompositeDisposable = null;
                                }
                            }, throwable -> {
                                Log.d(TAG, throwable.getMessage());
                                // Dispose
                                if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
                                    mCompositeDisposable.dispose();
                                    mCompositeDisposable = null;
                                }
                            }));
                } catch (Exception e) {
                    // Dispose
                    if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
                        mCompositeDisposable.dispose();
                        mCompositeDisposable = null;
                    }
                    isRefresh = false;
                    e.printStackTrace();
                }
            }
        }
    }
}
