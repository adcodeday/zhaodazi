package org.lu.zhaodazi.user.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.lu.zhaodazi.user.enums.LoginType;

@Getter
@Setter
public class LoginDTO {

    private String username;

    private String credential;

    private LoginType loginType = LoginType.NORMAL;
}
