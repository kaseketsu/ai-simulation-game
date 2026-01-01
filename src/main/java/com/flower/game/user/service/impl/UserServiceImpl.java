package com.flower.game.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flower.game.user.dao.UserMapper;
import com.flower.game.user.models.dto.*;
import com.flower.game.user.models.entity.MyUserDetails;
import com.flower.game.user.models.entity.User;
import com.flower.game.user.service.IUserService;
import com.flower.game.user.service.TokenService;
import common.annotations.ExceptionLog;
import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import common.manager.RedisManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户基础表 服务实现类
 * </p>
 *
 * @author F1ower
 * @since 2025-12-28
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final AuthenticationManager authenticationManager;

    private final MyUserDetailServiceImpl myUserDetailService;

    private final TokenService tokenService;

    private final RedisManager redisManager;

    @Value("${spring.jwt.access-token-prefix}")
    private String accessTokenPrefix;

    @Value("${spring.jwt.refresh-token-prefix}")
    private String refreshTokenPrefix;

    @Value("${spring.jwt.default-kick-out}")
    private Long defaultKickOut;

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @return jwt
     */
    @Override
    @ExceptionLog("用户登录失败")
    public JwtResponse userLogin(@NonNull UserLoginRequest loginRequest) {
        // 获取用户名和密码，用 AuthenticationManger 进行校验
        String userAccount = loginRequest.getUserAccount();
        String userPassword = loginRequest.getUserPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAccount, userPassword));
        // 获取用户信息
        MyUserDetails myUserDetails = myUserDetailService.loadUserByUsername(userAccount);
        // 获取并返回 jwt
        return tokenService.generateToken(myUserDetails);
    }

    /**
     * 获取新的 accessToken，并更新 refreshToken
     *
     * @param refreshRequest 刷新请求
     * @return 新的 token
     */
    @Override
    @ExceptionLog("token 刷新失败")
    public JwtResponse refreshToken(@NonNull TokenRefreshRequest refreshRequest) {
        // 获取 refreshToken
        String refreshToken = refreshRequest.getRefreshToken();
        // 根据 refreshToken 获取新的 token
        return tokenService.refreshToken(refreshToken);
    }



    /**
     * 踢用户下线
     *
     * @param userAccount 用户账号
     */
    @Override
    @ExceptionLog("用户下线失败")
    public void invalidateToken(@NonNull String userAccount) {
        // 获取 map
        Map<String, String> multiHash = redisManager.getMultiHash(userAccount, String.class, String.class);
        // 获取 token
        if (!Objects.isNull(multiHash)) {
            // 拉黑并删除 refreshToken
            String refreshToken = multiHash.get(refreshTokenPrefix);
            if (StrUtil.isNotBlank(refreshToken)) {
                tokenService.invalidateToken(refreshToken, defaultKickOut, TimeUnit.MILLISECONDS);
            }
            // 拉黑并删除 accessToken
            String accessToken = multiHash.get(accessTokenPrefix);
            if (StrUtil.isNotBlank(accessToken)) {
                tokenService.invalidateToken(accessToken, defaultKickOut, TimeUnit.MILLISECONDS);
            }
            // 删除 k-v 结构
            redisManager.deleteValue(userAccount);
        }
    }

    /**
     * 用户主动下线
     *
     * @param logoutRequest 下线请求
     */
    @Override
    public void userLogOut(@NonNull UserLogOutRequest logoutRequest) {
        invalidateToken(logoutRequest.getUserAccount());
    }
}
