package com.quoctrungdhqn.shiportalandroid.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("expires_in")
    @Expose
    private Integer expiresIn;
    @SerializedName("token_type")
    @Expose
    private String tokenType;
    @SerializedName("scope")
    @Expose
    private String scope;
    @SerializedName("refresh_token")
    @Expose
    private String refreshToken = "";
    @SerializedName("oauthUser")
    @Expose
    private OauthUser oauthUser;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public OauthUser getOauthUser() {
        return oauthUser;
    }

    public void setOauthUser(OauthUser oauthUser) {
        this.oauthUser = oauthUser;
    }

    public class OauthUser {

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("business_name")
        @Expose
        private String businessName;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("description")
        @Expose
        private Object description;
        @SerializedName("profile_pic")
        @Expose
        private Object profilePic;
        @SerializedName("passion")
        @Expose
        private Object passion;
        @SerializedName("dob")
        @Expose
        private Object dob;
        @SerializedName("gender")
        @Expose
        private Object gender;
        @SerializedName("scope")
        @Expose
        private String scope;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("email_verified")
        @Expose
        private String emailVerified;
        @SerializedName("email_verified_date")
        @Expose
        private String emailVerifiedDate;
        @SerializedName("activate_jti")
        @Expose
        private Object activateJti;
        @SerializedName("fcm_token")
        @Expose
        private Object fcmToken;
        @SerializedName("device_type")
        @Expose
        private Object deviceType;
        @SerializedName("access_token")
        @Expose
        private String accessToken;
        @SerializedName("client_id")
        @Expose
        private String clientId;
        @SerializedName("expires")
        @Expose
        private String expires;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getBusinessName() {
            return businessName;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
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

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public Object getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(Object profilePic) {
            this.profilePic = profilePic;
        }

        public Object getPassion() {
            return passion;
        }

        public void setPassion(Object passion) {
            this.passion = passion;
        }

        public Object getDob() {
            return dob;
        }

        public void setDob(Object dob) {
            this.dob = dob;
        }

        public Object getGender() {
            return gender;
        }

        public void setGender(Object gender) {
            this.gender = gender;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmailVerified() {
            return emailVerified;
        }

        public void setEmailVerified(String emailVerified) {
            this.emailVerified = emailVerified;
        }

        public String getEmailVerifiedDate() {
            return emailVerifiedDate;
        }

        public void setEmailVerifiedDate(String emailVerifiedDate) {
            this.emailVerifiedDate = emailVerifiedDate;
        }

        public Object getActivateJti() {
            return activateJti;
        }

        public void setActivateJti(Object activateJti) {
            this.activateJti = activateJti;
        }

        public Object getFcmToken() {
            return fcmToken;
        }

        public void setFcmToken(Object fcmToken) {
            this.fcmToken = fcmToken;
        }

        public Object getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(Object deviceType) {
            this.deviceType = deviceType;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getExpires() {
            return expires;
        }

        public void setExpires(String expires) {
            this.expires = expires;
        }

    }
}
