package com.flower.game.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flower.game.user.models.dto.*;
import com.flower.game.user.models.entity.User;
import io.jsonwebtoken.Jwt;
import jakarta.annotation.Nonnull;
import lombok.NonNull;

/**
 * <p>
 * 用户基础表 服务类
 * </p>
 *
 * @author F1ower
 * @since 2025-12-28
 */
public interface IUserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param userRegisterRequest 注册请求
     */
    void userRegister(@Nonnull final UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @return jwt
     */
    void userLogin(@NonNull final UserLoginRequest loginRequest);

//    /**
//     * 获取新的 accessToken，并更新 refreshToken
//     *
//     * @param refreshRequest 刷新请求
//     * @return 新的 token
//     */
//    JwtResponse refreshToken(@NonNull final TokenRefreshRequest refreshRequest);


//    /**
//     * 踢用户下线
//     *
//     * @param userAccount 用户账号
//     */
//    void invalidateToken(@NonNull final String userAccount);

//    /**
//     * 用户主动下线
//     *
//     * @param logoutRequest 下线请求
//     */
//    void userLogOut(@NonNull final UserLogOutRequest logoutRequest);
}
