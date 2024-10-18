package org.lu.zhaodazi.user.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
@Data
@Builder
public class TokenInfo implements Serializable {
    private Long uid;
    private String token;
    private String refreshToken;

}
