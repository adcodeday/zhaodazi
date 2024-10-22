package org.lu.zhaodazi.user.service;

import org.lu.zhaodazi.user.domain.entity.TokenInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public interface AuthService {
    TokenInfo login(AbstractAuthenticationToken authenticationToken);
}
