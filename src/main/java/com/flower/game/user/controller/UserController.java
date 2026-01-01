package com.flower.game.user.controller;

import cn.hutool.core.lang.Assert;
import com.flower.game.user.models.dto.JwtResponse;
import com.flower.game.user.models.dto.UserLoginRequest;
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
}
