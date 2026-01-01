package com.flower.game.user.service;


import com.flower.game.user.models.dto.JwtResponse;
import com.flower.game.user.models.entity.MyUserDetails;
import lombok.NonNull;

import java.util.concurrent.TimeUnit;

public interface TokenService {

    /**
     * 生成 access / refresh token
     *
     * @param userDetails 用户详情
     * @return token 响应
     */
    JwtResponse generateToken(@NonNull final MyUserDetails userDetails);

    /**
     * 根据刷新 token 拿 accessToken
     *
     * @param refreshToken 刷新 token
     * @return token 响应
     */
    JwtResponse refreshToken(@NonNull final String refreshToken);

    /**
     * 使 token 失效
     *
     * @param token 待失效 token
     */
    void invalidateToken(@NonNull final String token, Long expiration, TimeUnit timeUnit);
}
