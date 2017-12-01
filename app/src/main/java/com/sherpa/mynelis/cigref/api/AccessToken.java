package com.sherpa.mynelis.cigref.api;


import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Date;


public class AccessToken {

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("expires")
    private int expires;
    @SerializedName("expires_in")
    private int expiresIn;
    @SerializedName("refresh_token")
    private String refreshToken;

    private Date createdAt;
    private Date needRefreshAt;

    public AccessToken() {
        this("", "", 0, 0, "");
    }

    public AccessToken(String accessToken, String tokenType, int expires, int expiresIn, String refreshToken) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expires = expires;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
    }

    public void init(){
        createdAt = new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, expiresIn - 500);
        needRefreshAt = c.getTime();
    }

    public boolean needRefresh(){
        Date now = new Date();
        return now.after(this.needRefreshAt);
    }

    /* Getters & setters */
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

