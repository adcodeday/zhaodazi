package org.lu.zhaodazi.user.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class User implements UserDetails {
    private Long id;

    private String username;

    private String password;

    private String email;

    private boolean enabled;

    private Collection<GrantedAuthority> authorities;

}
