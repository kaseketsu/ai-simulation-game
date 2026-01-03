package com.flower.game.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flower.game.auth.models.dto.RoleAddRequest;
import com.flower.game.auth.models.entity.Role;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author F1ower
 * @since 2025-12-28
 */
public interface IRoleService extends IService<Role> {

    /**
     * 添加角色
     *
     * @param addRequest 添加请求
     */
    void addRole(RoleAddRequest addRequest);
}
