package com.quoctrungdhqn.shiportalandroid.data;

import com.quoctrungdhqn.shiportalandroid.data.request.LoginRequest;
import com.quoctrungdhqn.shiportalandroid.data.response.LoginResponse;
import com.quoctrungdhqn.shiportalandroid.data.response.UserResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceAPI {
    @POST("/user/token")
    Observable<Response<LoginResponse>> login(@Body LoginRequest loginRequest);

    @GET("/user")
    Observable<Response<UserResponse>> getUsers(@Query("page") int page);
}
