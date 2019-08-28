package com.dingjust.platform.klotho.storefront.security.http;

import java.io.Serializable;

/**
 * @author lzh
 * @version 1.0
 * @since JDK1.8
 */
public class DjLoginResponse implements Serializable {
    private String username;
    private String displayName;
    private String token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
