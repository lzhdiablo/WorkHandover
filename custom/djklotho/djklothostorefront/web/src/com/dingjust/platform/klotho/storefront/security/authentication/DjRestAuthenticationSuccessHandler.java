package com.dingjust.platform.klotho.storefront.security.authentication;

import com.dingjust.platform.klotho.storefront.security.http.DjLoginResponse;
import com.google.gson.Gson;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lzh
 * @version 1.0
 * @since JDK1.8
 */
public class DjRestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private UserService userService;
    private DjTokenManager djTokenManager;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication
    ) throws IOException {
        UserModel currentUser = userService.getUserForUID(authentication.getName());
        userService.setCurrentUser(currentUser);

        DjLoginResponse body = new DjLoginResponse();
        body.setUsername(authentication.getName());
        body.setDisplayName(currentUser.getName());
        body.setToken(djTokenManager.createToken(authentication).getToken());

        response.getWriter().write(new Gson().toJson(body));
        response.flushBuffer();
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setDjTokenManager(DjTokenManager djTokenManager) {
        this.djTokenManager = djTokenManager;
    }
}
