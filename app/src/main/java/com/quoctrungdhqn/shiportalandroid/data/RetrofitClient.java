package com.quoctrungdhqn.shiportalandroid.data;

import android.content.Context;
import android.text.TextUtils;

import com.quoctrungdhqn.shiportalandroid.BuildConfig;
import com.quoctrungdhqn.shiportalandroid.utils.SharedPrefs;

import java.util.Locale;

import okhttp3.CacheControl;
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
                String token_type = SharedPrefs.getStringPrefs(context, "token_type");
                if (!TextUtils.isEmpty(access_token) && !TextUtils.isEmpty(token_type)) {
                    String header = String.format(Locale.getDefault(), "%s %s", token_type, access_token);
                    newBuilder.cacheControl(CacheControl.FORCE_NETWORK).addHeader("Authorization", header);
                }
                return chain.proceed(newBuilder.build());
            });

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
