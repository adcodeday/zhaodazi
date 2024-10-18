package org.lu.zhaodazi.user.controller;

import org.lu.zhaodazi.common.domain.vo.res.ApiResult;
import org.lu.zhaodazi.user.domain.dto.LoginDTO;
import org.lu.zhaodazi.user.domain.entity.TokenInfo;
import org.lu.zhaodazi.user.security.authtication.EmailAuthenticationToken;
import org.lu.zhaodazi.user.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;
    //TODO 发送验证码接口,模拟发送
    @GetMapping("/emailcode")
    public ApiResult<String> emailCode(String email){
        String s = authService.sendVerifyCode(email);
        return ApiResult.success(s);
    }
    @PostMapping("/login")
    public ApiResult<?> login(@RequestBody LoginDTO loginDTO){
        TokenInfo tokenInfo;
        switch (loginDTO.getLoginType()) {
            case NORMAL:
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getCredential());
                tokenInfo = authService.login(authenticationToken);
                break;
            case EMAIL:
                EmailAuthenticationToken emailAuthenticationToken = new EmailAuthenticationToken(loginDTO.getUsername(), loginDTO.getCredential());
                tokenInfo = authService.login(emailAuthenticationToken);
                break;
            default:
                throw new UnsupportedOperationException("不支持的登录方式:" + loginDTO.getLoginType());
        }
        return ApiResult.success(tokenInfo);
    }
}
