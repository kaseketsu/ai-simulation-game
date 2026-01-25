//package com.flower.game.user.service.impl;
//
//import cn.hutool.core.util.StrUtil;
//import com.flower.game.user.models.dto.JwtResponse;
//import com.flower.game.user.models.entity.MyUserDetails;
//import com.flower.game.user.service.TokenService;
//import common.exceptions.BusinessException;
//import common.exceptions.ErrorCode;
//import common.manager.JwtManager;
//import common.manager.RedisManager;
//import jakarta.annotation.Resource;
//import lombok.NonNull;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//import java.util.concurrent.TimeUnit;
//
//@Service
//@Slf4j
//public class TokenServiceImpl implements TokenService {
//
//    @Resource
//    private JwtManager jwtManager;
//
//    @Resource
//    private RedisManager redisManager;
//
//    @Resource
//    private MyUserDetailServiceImpl userDetailsService;
//
//    /**
//     * 刷新 token 前缀
//     */
//    @Value("${spring.jwt.refresh-token-expire}")
//    private String refreshTokenPrefix;
//
//    /**
//     * 刷新 token 过期时间
//     */
//    @Value("${spring.jwt.refresh-token-expire}")
//    private long refreshTokenExpire;
//
//    /**
//     * 请求 token 过期时间
//     */
//    @Value("${spring.jwt.access-token-expire}")
//    private long accessTokenExpire;
//
//    /**
//     * 黑名单前缀
//     */
//    @Value("${spring.security.black-list-prefix}")
//    private String blackListPrefix;
//
//    /**
//     * 黑名单默认过期时长
//     */
//    @Value("${spring.security.black-list-expiration}")
//    private long blackListExpiration;
//
//    @Value("${spring.jwt.access-token-prefix}")
//    private String accessTokenPrefix;
//
//    /**
//     * 根据用户信息生成 token
//     *
//     * @param userAccount 用户账号
//     * @return token 响应
//     */
//    @Override
//    public JwtResponse generateToken(@NonNull final String userAccount) {
//        try {
//            // 获得用户详情
//            MyUserDetails userDetails = userDetailsService.loadUserByUsername(userAccount);
//            // 生成 token
//            String accessToken = jwtManager.generateAccessToken(userDetails);
//            String refreshToken = jwtManager.generateRefreshToken(userDetails);
//            // refreshToken 放入 redis
//            String jti = jwtManager.extractJti(refreshToken);
//            String key = String.format("%s:%s", refreshTokenPrefix, jti);
//            String value = String.format("%s:%s", userDetails.getUsername(), System.currentTimeMillis());
//            redisManager.addValue(key, value, accessTokenExpire, TimeUnit.MILLISECONDS);
//            // 建立 userAccount -> jti 集合映射, 存入 accessToken 和 refreshToken，后续借此踢下线
//            Map<String, Object> accountMapping = new HashMap<>();
//            accountMapping.put(refreshTokenPrefix, jti);
//            accountMapping.put(accessToken, jwtManager.extractJti(accessToken));
//            redisManager.addMultiHash(userDetails.getUserAccount(), accountMapping);
//            // 封装响应并返回
//            JwtResponse jwtResponse = new JwtResponse();
//            jwtResponse.setAccessToken(accessToken);
//            jwtResponse.setRefreshToken(refreshToken);
//            jwtResponse.setAccessTokenExpire(accessTokenExpire);
//            jwtResponse.setRefreshTokenExpire(refreshTokenExpire);
//            return jwtResponse;
//        } catch (Exception e) {
//            String errorMsg = String.format("生成 token 失败，原因是: %s", e.getMessage());
//            log.error(errorMsg, e);
//            throw new BusinessException(ErrorCode.GENERATE_ERROR, errorMsg);
//        }
//    }
//
//    /**
//     * 跟觉刷新 token 获取新 accessToken
//     *
//     * @param refreshToken 刷新 token
//     * @return 新 token
//     */
//    @Override
//    public JwtResponse refreshToken(@NonNull final String refreshToken) {
//        try {
//            // 校验 refreshToken 是否合法
//            if (StrUtil.isBlank(refreshToken)) {
//                throw new BusinessException(ErrorCode.PARAM_ERROR, "token 为空");
//            }
//            String jti = jwtManager.extractJti(refreshToken);
//            String key = String.format("%s:%s", refreshTokenPrefix, jti);
//            if (redisManager.hasKey(key)) {
//                throw new BusinessException(ErrorCode.EXPIRE_ERROR, "token 已过期");
//            }
//            // 校验 refreshToken 是否过期
//            String userName = jwtManager.extractUserName(refreshToken);
//            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
//            if (!jwtManager.isTokenExpired(refreshToken)) {
//                throw new BusinessException(ErrorCode.AUTHORIZE_ERROR, "无权限");
//            }
//            // 生成新 accessToken
//            String accessToken = jwtManager.generateAccessToken(userDetails);
//            // 删除旧 refreshToken，存储新 refreshToken
//            String value = redisManager.getValue(key, String.class);
//            String[] parts = value.split(":");
//            long addTime = Long.parseLong(parts[1]);
//            long remainTime = System.currentTimeMillis() - addTime;
//            String newRefreshToken;
//            if (remainTime > 0L) {
//                newRefreshToken = jwtManager.generateRefreshToken(userDetails);
//                String newJti = jwtManager.extractJti(newRefreshToken);
//                String newValue = String.format("%s:%s", userDetails.getUsername(), System.currentTimeMillis());
//                String newKey = String.format("%s:%s", refreshTokenPrefix, newJti);
//                redisManager.addValue(newKey, newValue, remainTime, TimeUnit.MILLISECONDS);
//            } else {
//                throw new BusinessException(ErrorCode.EXPIRE_ERROR, "token 已过期");
//            }
//            // 建立 userAccount -> jti 集合映射, 存入 accessToken 和 refreshToken，后续借此踢下线
//            Map<String, Object> accountMapping = new HashMap<>();
//            accountMapping.put(refreshTokenPrefix, jti);
//            accountMapping.put(accessToken, jwtManager.extractJti(accessToken));
//            redisManager.addMultiHash(userName, accountMapping);
//            // 返回响应
//            JwtResponse jwtResponse = new JwtResponse();
//            jwtResponse.setAccessToken(accessToken);
//            jwtResponse.setRefreshToken(newRefreshToken);
//            jwtResponse.setAccessTokenExpire(accessTokenExpire);
//            jwtResponse.setRefreshTokenExpire(remainTime);
//            return jwtResponse;
//        } catch (Exception e) {
//            String errorMsg = String.format("刷新 token 失败，原因是: %s", e.getMessage());
//            log.error(errorMsg, e);
//            throw new BusinessException(ErrorCode.GENERATE_ERROR, errorMsg);
//        }
//    }
//
//    /**
//     * 将某个 token 失效（踢下线）
//     *
//     * @param token 待失效 token
//     */
//    @Override
//    public void invalidateToken(@NonNull final String token, Long expiration, TimeUnit timeUnit) {
//        try {
//            // 先判断 token 是否有效
//            if (StrUtil.isBlank(token)) {
//                return;
//            }
//            if (jwtManager.isTokenExpired(token)) {
//                return;
//            }
//            String userName = jwtManager.extractUserName(token);
//            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
//            if (!jwtManager.isValidToken(token, userDetails)) {
//                return;
//            }
//            String jti = jwtManager.extractJti(token);
//            if (redisManager.isInBlackList(jti)) {
//                return;
//            }
//            // 将 token 放入黑名单
//            String key = String.format("%s:%s", blackListPrefix, jti);
//            expiration = Objects.isNull(expiration) ? blackListExpiration : expiration;
//            timeUnit = Objects.isNull(timeUnit) ? TimeUnit.MICROSECONDS : timeUnit;
//            String value = String.format("%s:%s", userDetails.getUsername(), System.currentTimeMillis());
//            redisManager.addValue(key, value, expiration, timeUnit);
//            // 如果是 refreshToken，同时删除 redis 持久化数据
//            String refreshKey = String.format("%s:%s", refreshTokenPrefix, jti);
//            if (redisManager.hasKey(refreshKey)) {
//                redisManager.deleteValue(refreshKey);
//            }
//        } catch (Exception e) {
//            String errorMsg = String.format("失效 token 失败，原因是: %s", e.getMessage());
//            log.error(errorMsg, e);
//            throw new BusinessException(ErrorCode.GENERATE_ERROR, errorMsg);
//        }
//    }
//}
