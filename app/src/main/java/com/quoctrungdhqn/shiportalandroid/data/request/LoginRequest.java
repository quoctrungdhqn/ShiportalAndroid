package com.quoctrungdhqn.shiportalandroid.data.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginRequest implements Serializable {
    @SerializedName("client_id")
    private String clienId;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("grant_type")
    private String grant_type;

    public LoginRequest(String clienId, String username, String password, String grant_type) {
        this.clienId = clienId; // default = 1
        this.username = username; // default = your_email
        this.password = password; // default = password
        this.grant_type = grant_type; // default = password
    }
}
