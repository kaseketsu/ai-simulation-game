package com.flower.game.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flower.game.user.dao.RolePermissionMapper;
import com.flower.game.user.models.entity.RolePermission;
import com.flower.game.user.service.IRolePermissionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色权限关联表 服务实现类
 * </p>
 *
 * @author F1ower
 * @since 2025-12-28
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements IRolePermissionService {

}
