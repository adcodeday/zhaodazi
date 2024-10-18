package org.lu.zhaodazi.user.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.lu.zhaodazi.common.util.RedisUtil;
import org.lu.zhaodazi.user.domain.entity.TokenInfo;
import org.lu.zhaodazi.user.domain.entity.User;
import org.lu.zhaodazi.user.service.AuthService;
import org.lu.zhaodazi.user.service.TokenService;
import org.lu.zhaodazi.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Random;
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;

    @Override
    public String sendVerifyCode(String email) {
        //TODO 模拟发送
        String s = generateRandomNumber(6);
        RedisUtil.set("EMAIL_CODE:"+email,s,60);
        log.info(email+"-验证码-"+s);
        return s;
    }

    @Override
    public TokenInfo login(AbstractAuthenticationToken authenticationToken) {
        Authentication authenticate;
        if ((authenticate = SecurityContextHolder.getContext().getAuthentication()) == null || authenticate instanceof AnonymousAuthenticationToken) {
            authenticate = authenticationManager.authenticate(authenticationToken);
        }
        User user = (User)authenticate.getPrincipal();
        TokenInfo tokenInfo = tokenService.generate(user);
        log.info("存入缓存："+user.getId());
        RedisUtil.set("USER_"+user.getId(),user,3600);
        return tokenInfo;
    }

    public static String generateRandomNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // 生成0到9之间的随机数并追加到字符串
        }

        return sb.toString();
    }
}

