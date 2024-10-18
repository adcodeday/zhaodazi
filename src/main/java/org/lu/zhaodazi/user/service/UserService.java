package org.lu.zhaodazi.user.service;

import org.lu.zhaodazi.user.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
    boolean save(User user);

    User findByUsername(String username);

    User findByUserId(String userId);

    User loadUserByUserId(Long userId) throws UsernameNotFoundException;

    User loadUserByEmail(String email) throws UsernameNotFoundException;
}
