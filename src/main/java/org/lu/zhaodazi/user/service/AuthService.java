package org.lu.zhaodazi.user.service;

import org.lu.zhaodazi.user.domain.entity.TokenInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public interface AuthService {
    //TODO 发送验证码，模拟发送
    String sendVerifyCode(String email);
    TokenInfo login(AbstractAuthenticationToken authenticationToken);
}
