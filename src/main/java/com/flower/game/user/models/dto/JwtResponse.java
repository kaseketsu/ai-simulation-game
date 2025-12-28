package com.flower.game.user.models.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * jwt 响应
 */
@Data
public class JwtResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 2770742142781401631L;

    /**
     * 请求 token
     */
    private String accessToken;

    /**
     * 请求 token 过期时间
     */
    private Long accessTokenExpire;

    /**
     * 刷新 token
     */
    private String refreshToken;

    /**
     * 刷新 token 过期时间
     */
    private Long refreshTokenExpire;
}
