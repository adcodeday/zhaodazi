package org.lu.zhaodazi.user.service.Impl;

import org.lu.zhaodazi.user.dao.UserMapper;
import org.lu.zhaodazi.user.domain.entity.User;
import org.lu.zhaodazi.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public boolean save(User user) {
        return false;
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.loadUserByUsername(username);
    }

    @Override
    public User findByUserId(String userId) {
        return null;
    }

    @Override
    public User loadUserByUserId(Long userId) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public User loadUserByEmail(String email) throws UsernameNotFoundException {
        User user = userMapper.loadUserByEmail(email);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
