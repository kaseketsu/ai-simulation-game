package com.flower.game.user.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flower.game.user.dao.PermissionMapper;
import com.flower.game.user.models.entity.Permission;
import com.flower.game.user.service.IPermissionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author F1ower
 * @since 2025-12-28
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

}
