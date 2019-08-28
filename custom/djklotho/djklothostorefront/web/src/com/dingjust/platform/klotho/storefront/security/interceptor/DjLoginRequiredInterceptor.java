package com.dingjust.platform.klotho.storefront.security.interceptor;

import com.dingjust.platform.klotho.storefront.security.annotation.DjLoginRequired;
import com.dingjust.platform.klotho.storefront.security.authentication.DjTokenDTO;
import com.dingjust.platform.klotho.storefront.security.authentication.DjTokenManager;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DjLoginRequiredInterceptor extends HandlerInterceptorAdapter {
    private DjTokenManager djTokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            DjLoginRequired loginRequiredAnnotation = ((HandlerMethod) handler).getMethodAnnotation(DjLoginRequired.class);
            if (ObjectUtils.anyNotNull(loginRequiredAnnotation)) {
                String token = request.getHeader("TOKEN");
                if (token == null) {
                    response.setStatus(HttpStatus.SC_UNAUTHORIZED);
                    return false;
                }
                DjTokenDTO tokenDTO = djTokenManager.getToken(token);
                if (tokenDTO == null
                        || (tokenDTO.getExpirationTimeMillis() < System.currentTimeMillis() && djTokenManager.clearToken(token))) {
                    response.setStatus(HttpStatus.SC_UNAUTHORIZED);
                    return false;
                }
            }
        }
        return true;
    }

    public void setDjTokenManager(DjTokenManager djTokenManager) {
        this.djTokenManager = djTokenManager;
    }
}
