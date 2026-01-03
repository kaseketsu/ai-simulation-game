package com.flower.game.user.controller;

import cn.hutool.core.lang.Assert;
import com.flower.game.user.models.dto.*;
import com.flower.game.user.service.IUserService;
import common.annotations.ApiErrorCode;
import common.baseEntities.BaseResponse;
import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import common.utils.ResultUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户相关控制器
 *
 * @author F1ower
 * @since 2026-1-1
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求
     * @return jwt
     */
    @PostMapping("/register")
    @ApiErrorCode(ErrorCode.REGISTER_ERROR)
    public BaseResponse<String> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        Assert.notNull(userRegisterRequest, "请求参数不能为空");
        userService.userRegister(userRegisterRequest);
        return ResultUtils.success("注册成功");
    }

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @return jwt
     */
    @PostMapping("/login")
    @ApiErrorCode(ErrorCode.LOGIN_ERROR)
    public BaseResponse<JwtResponse> userLogin(@RequestBody UserLoginRequest loginRequest) {
        Assert.notNull(loginRequest, "请求参数不能为空");
        JwtResponse jwtResponse = userService.userLogin(loginRequest);
        return ResultUtils.success(jwtResponse);
    }

    /**
     * 获取新的 accessToken，并更新 refreshToken
     *
     * @param refreshRequest 刷新请求
     * @return 新的 token
     */
    @PostMapping("/refresh")
    @ApiErrorCode(ErrorCode.REFRESH_ERROR)
    public BaseResponse<JwtResponse> refreshToken(@RequestBody TokenRefreshRequest refreshRequest) {
        Assert.notNull(refreshRequest, "请求参数不能为空");
        JwtResponse jwtResponse = userService.refreshToken(refreshRequest);
        return ResultUtils.success(jwtResponse);
    }

    /**
     * 用户下线
     *
     * @param logOutRequest 用户下线请求
     * @return res
     */
    @PostMapping("/logout")
    @ApiErrorCode(ErrorCode.LOG_OUT_ERROR)
    public BaseResponse<String> userLogout(@RequestBody UserLogOutRequest logOutRequest) {
        Assert.notNull(logOutRequest, "请求参数不能为空");
        userService.userLogOut(logOutRequest);
        return ResultUtils.success("账号已下线");
    }

    /**
     * 踢用户下线（管理员）
     *
     * @param invalidateTokenRequest token 失效请求
     * @return res
     */
    @PostMapping("/invalidate")
    @ApiErrorCode(ErrorCode.INVALIDATE_ERROR)
    public BaseResponse<String> invalidateToken(@RequestBody InvalidateTokenRequest invalidateTokenRequest) {
        Assert.notNull(invalidateTokenRequest, "请求参数不能为空");
        userService.invalidateToken(invalidateTokenRequest.getUserAccount());
        return ResultUtils.success("账号已下线");
    }
}
