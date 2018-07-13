package com.quoctrungdhqn.shiportalandroid.data.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetailResponse implements Parcelable {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("description")
    @Expose
    private String description;

    private UserDetailResponse(Parcel in) {
        userId = in.readString();
        businessName = in.readString();
        lastName = in.readString();
        mobile = in.readString();
        email = in.readString();
        description = in.readString();
    }

    public static final Creator<UserDetailResponse> CREATOR = new Creator<UserDetailResponse>() {
        @Override
        public UserDetailResponse createFromParcel(Parcel in) {
            return new UserDetailResponse(in);
        }

        @Override
        public UserDetailResponse[] newArray(int size) {
            return new UserDetailResponse[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(businessName);
        dest.writeString(lastName);
        dest.writeString(mobile);
        dest.writeString(email);
        dest.writeString(description);
    }
}
