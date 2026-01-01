package com.flower.game.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flower.game.user.dao.UserMapper;
import com.flower.game.user.models.dto.JwtResponse;
import com.flower.game.user.models.dto.UserLoginRequest;
import com.flower.game.user.models.entity.MyUserDetails;
import com.flower.game.user.models.entity.User;
import com.flower.game.user.service.IUserService;
import com.flower.game.user.service.TokenService;
import common.annotations.ExceptionLog;
import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

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
        // 获取 jwt
        JwtResponse jwtResponse = tokenService.generateToken(myUserDetails);
        // 返回 jwt
        return jwtResponse;
    }
}
