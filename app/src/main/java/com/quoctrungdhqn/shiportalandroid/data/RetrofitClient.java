package com.quoctrungdhqn.shiportalandroid.data;

import android.content.Context;
import android.text.TextUtils;

import com.quoctrungdhqn.shiportalandroid.BuildConfig;
import com.quoctrungdhqn.shiportalandroid.utils.SharedPrefs;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static ServiceAPI serviceAPI;

    public static ServiceAPI getServiceAPI(Context context) {
        if (serviceAPI == null) {
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.addInterceptor(loggingInterceptor);
            client.addInterceptor(chain -> {
                Request request = chain.request();
                Request.Builder newBuilder = request.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("User-Agent", "Android")
                        .addHeader("Content-Type", "application/json");

                // Get access_token & token_type
                String access_token = SharedPrefs.getStringPrefs(context, "access_token");

                if (!TextUtils.isEmpty(access_token)) {
                    String header = String.format(Locale.getDefault(), "Bearer %s", access_token);
                    newBuilder.addHeader("Authorization", header);
                }
                return chain.proceed(newBuilder.build());
            })
                    .addInterceptor(new RefreshingOAuthToken(context)) // Refresh token
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.HOST)
                    .client(client.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            serviceAPI = retrofit.create(ServiceAPI.class);
        }

        return serviceAPI;
    }
}
