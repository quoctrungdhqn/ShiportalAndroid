package com.quoctrungdhqn.shiportalandroid.data;

import com.quoctrungdhqn.shiportalandroid.data.request.LoginRequest;
import com.quoctrungdhqn.shiportalandroid.data.request.RefreshTokenRequest;
import com.quoctrungdhqn.shiportalandroid.data.response.LoginResponse;
import com.quoctrungdhqn.shiportalandroid.data.response.RefreshTokenResponse;
import com.quoctrungdhqn.shiportalandroid.data.response.UserDetailResponse;
import com.quoctrungdhqn.shiportalandroid.data.response.UserResponse;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceAPI {
    @POST("/user/token")
    Observable<Response<LoginResponse>> login(@Body LoginRequest loginRequest);

    @POST("/user/token")
    Single<Response<RefreshTokenResponse>> refreshToken(@Body RefreshTokenRequest refreshTokenRequest);

    @GET("/user")
    Observable<Response<UserResponse>> getUsers(@Query("page") int page);

    @GET("/user/{id}")
    Observable<Response<UserDetailResponse>> getUserDetail(@Path("id") String id);
}
