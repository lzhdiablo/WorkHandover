package com.dingjust.platform.klotho.storefront.security.authentication;

import de.hybris.platform.util.Config;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class DjTokenManager {
     private static final String WEB_APPLICATION_TOKEN_LAST_TIME = "web.application.token.last.time";
     protected static final ConcurrentHashMap<String, DjTokenDTO> tokenPool = new ConcurrentHashMap<>();

     public DjTokenDTO createToken(Authentication authentication) throws UnsupportedEncodingException {
          DjTokenDTO tokenDTO = new DjTokenDTO();
          UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
          tokenDTO.setToken(Base64Utils
                  .encodeToString((authentication.getName() + ":" + token.getCredentials()).getBytes("UTF-8")));
          tokenDTO.setUserId(authentication.getName());
          tokenDTO.setExpirationTimeMillis(System.currentTimeMillis()
                  + Long.valueOf(Config.getInt(WEB_APPLICATION_TOKEN_LAST_TIME, 24) * 3600 * 1000));
          tokenPool.put(tokenDTO.getToken(), tokenDTO);
          return tokenDTO;
     }

     public boolean clearToken(String token) {
          tokenPool.remove(token);
          return true;
     }

     public DjTokenDTO getToken(String token) {
          return tokenPool.get(token);
     }
}
