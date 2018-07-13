package com.quoctrungdhqn.shiportalandroid.data.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RefreshTokenRequest implements Serializable {
    @SerializedName("client_id")
    private String clienId;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("grant_type")
    private String grant_type;

    @SerializedName("refresh_token")
    private String refresh_token;

    public RefreshTokenRequest(String clienId, String username, String password, String grant_type, String refresh_token) {
        this.clienId = clienId; // default = 1
        this.username = username; // default = api
        this.password = password; // default = password
        this.grant_type = grant_type; // default = refresh_token
        this.refresh_token = refresh_token;
    }
}
