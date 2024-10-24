package org.lu.zhaodazi.auth.provider;

import org.lu.zhaodazi.auth.authtication.WxAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class WxAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //TODO 创建长链接
        //TODO
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(WxAuthenticationToken.class);
    }
}
