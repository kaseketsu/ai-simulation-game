package com.flower.game.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flower.game.user.models.dto.JwtResponse;
import com.flower.game.user.models.dto.UserLoginRequest;
import com.flower.game.user.models.entity.User;
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
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @return jwt
     */
    public JwtResponse userLogin(@NonNull final UserLoginRequest loginRequest);
}
