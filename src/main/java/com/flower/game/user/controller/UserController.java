package com.flower.game.user.controller;

import cn.hutool.core.lang.Assert;
import com.flower.game.user.models.dto.*;
import com.flower.game.user.service.IRoleService;
import com.flower.game.user.service.IUserService;
import common.annotations.ExceptionLog;
import common.baseEntities.BaseResponse;
import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import common.utils.ResultUtils;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
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
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @return jwt
     */
    @PostMapping("/login")
    public BaseResponse<JwtResponse> userLogin(@Valid @NonNull @RequestBody UserLoginRequest loginRequest) {
        try {
            Assert.notNull(loginRequest, "请求参数不能为空");
            JwtResponse jwtResponse = userService.userLogin(loginRequest);
            return ResultUtils.success(jwtResponse);
        } catch (Exception ex) {
            throw new BusinessException(ErrorCode.LOGIN_ERROR);
        }
    }

    /**
     * 获取新的 accessToken，并更新 refreshToken
     *
     * @param refreshRequest 刷新请求
     * @return 新的 token
     */
    @PostMapping("/refresh")
    public BaseResponse<JwtResponse> refreshToken(@Valid @NonNull @RequestBody TokenRefreshRequest refreshRequest) {
        try {
            Assert.notNull(refreshRequest, "请求参数不能为空");
            JwtResponse jwtResponse = userService.refreshToken(refreshRequest);
            return ResultUtils.success(jwtResponse);
        } catch (Exception ex) {
            throw new BusinessException(ErrorCode.LOGIN_ERROR);
        }
    }

    /**
     * 用户下线
     *
     * @param logOutRequest 用户下线请求
     * @return res
     */
    @PostMapping("/logout")
    public BaseResponse<String> userLogout(@Valid @NonNull @RequestBody UserLogOutRequest logOutRequest) {
        try {
            Assert.notNull(logOutRequest, "请求参数不能为空");
            userService.userLogOut(logOutRequest);
            return ResultUtils.success("账号已下线");
        } catch (Exception ex) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
    }

    /**
     * 踢用户下线（管理员）
     *
     * @param invalidateTokenRequest token 失效请求
     * @return res
     */
    @PostMapping("/invalidate")
    public BaseResponse<String> invalidateToken(@Valid @NonNull @RequestBody InvalidateTokenRequest invalidateTokenRequest) {
        try {
            Assert.notNull(invalidateTokenRequest, "请求参数不能为空");
            userService.invalidateToken(invalidateTokenRequest.getUserAccount());
            return ResultUtils.success("账号已下线");
        } catch (Exception ex) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
    }
}
