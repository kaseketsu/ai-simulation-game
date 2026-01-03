package com.flower.game.auth.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flower.game.auth.dao.RoleMapper;
import com.flower.game.auth.models.dto.RoleAddRequest;
import com.flower.game.auth.models.entity.Role;
import com.flower.game.auth.service.IRoleService;
import common.annotations.ExceptionLog;
import common.exceptions.ErrorCode;
import common.utils.ParamsCheckUtils;
import common.utils.ThrowUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author F1ower
 * @since 2025-12-28
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    /**
     * 添加角色
     *
     * @param addRequest 添加请求
     */
    @Override
    @ExceptionLog("角色添加失败")
    public void addRole(RoleAddRequest addRequest) {
        // 参数校验
        Assert.notNull(addRequest, "请求参数为空");
        String roleName = addRequest.getRoleName();
        String roleCode = addRequest.getRoleCode();
        ParamsCheckUtils.checkAll(roleName, roleCode);
        // 查看 roleCode 是否重复
        LambdaQueryWrapper<Role> roleWrapper = new LambdaQueryWrapper<>();
        roleWrapper.eq(Role::getRoleCode, roleCode)
                .eq(Role::getIsDeleted, 0);
        long count = this.count(roleWrapper);
        ThrowUtils.throwIf(count > 0, ErrorCode.ALREADY_EXIST_ERROR, "角色已存在");
        // 添加角色
        Role role = new Role();
        role.setRoleName(roleName);
        role.setRoleCode(roleCode);
        boolean saveRes = this.save(role);
        ThrowUtils.throwIf(!saveRes, ErrorCode.ROLE_ADD_ERROR, "角色保存失败");
    }
}
