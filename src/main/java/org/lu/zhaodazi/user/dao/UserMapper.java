package org.lu.zhaodazi.user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.lu.zhaodazi.user.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

@Mapper
public interface UserMapper {
    User loadUserByUsername(String username);
    @Select("select id,email from user where email=#{email}")
    User loadUserByEmail(String email);
}
