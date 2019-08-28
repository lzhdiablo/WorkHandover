package com.dingjust.platform.klotho.storefront.security.authentication;

public class DjTokenDTO {
    private String token;
    private Long expirationTimeMillis;
    private String userId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpirationTimeMillis() {
        return expirationTimeMillis;
    }

    public void setExpirationTimeMillis(Long expirationTimeMillis) {
        this.expirationTimeMillis = expirationTimeMillis;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
