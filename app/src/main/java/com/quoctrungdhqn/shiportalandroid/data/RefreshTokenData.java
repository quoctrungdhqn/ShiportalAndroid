package com.quoctrungdhqn.shiportalandroid.data;

import android.content.Context;

import com.quoctrungdhqn.shiportalandroid.data.response.LoginResponse;
import com.quoctrungdhqn.shiportalandroid.utils.SharedPrefs;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RefreshTokenData implements Interceptor {
    private LoginResponse loginResponse = null;
    private boolean isRefresh = false;
    private String refreshToken = null;
    private String token_type = null;
    private Context context;

    public RefreshTokenData(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        token_type = SharedPrefs.getStringPrefs(context, "token_type");

        Request.Builder builder = request.newBuilder();
        builder.header("Authorization", token_type);

        String token = SharedPrefs.getStringPrefs(context, "refresh_token");
        setAuthHeader(builder, token);

        request = builder.build();
        Response response = chain.proceed(request);

        // The access token provided has expired
        if (response.code() == 401) {
            synchronized (this) {
                
            }
        }
        return null;
    }

    private void setAuthHeader(Request.Builder builder, String token) {
        if (token != null) {
            token_type = SharedPrefs.getStringPrefs(context, "token_type");
            builder.header("Authorization", String.format("%s %s", token_type, token));
        }
    }
}
