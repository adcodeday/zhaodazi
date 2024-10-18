package org.lu.zhaodazi.user.service.Impl;

import org.lu.zhaodazi.common.exception.CommonException;
import org.lu.zhaodazi.common.util.JwtUtil;
import org.lu.zhaodazi.user.domain.entity.TokenInfo;
import org.lu.zhaodazi.user.domain.entity.User;
import org.lu.zhaodazi.user.service.TokenService;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService{

    @Override
    public TokenInfo generate(User user) {
        if(user==null||user.getId()==null){
            throw new CommonException("token-用户为空或用户id为空");
        }
        String token = JwtUtil.createToken(user.getId());
        return TokenInfo.builder().uid(user.getId()).token(token).build();
    }
}
