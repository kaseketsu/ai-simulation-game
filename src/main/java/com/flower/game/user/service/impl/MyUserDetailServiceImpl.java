package com.flower.game.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flower.game.user.models.entity.*;
import com.flower.game.user.service.*;
import common.annotations.ExceptionLog;
import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 自定义 security userDetailService 实现
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MyUserDetailServiceImpl implements UserDetailsService {

    private final IUserService userService;

    private final IUserRoleService userRoleService;

    private final IRoleService roleService;

    /**
     * 根据名称加载 userDetail
     *
     * @param userAccount 用户账号
     * @return MyUserDetails 自定义 userDetail
     */
    @ExceptionLog("获取用户详情失败")
    @Override
    public MyUserDetails loadUserByUsername(@NonNull final String userAccount) throws UsernameNotFoundException {
        try {
            Assert.notBlank(userAccount, "用户账号不能为空");
            LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.eq(User::getUserName, userAccount)
                    .eq(User::getIsDeleted, 0);
            User user = userService.getOne(userWrapper);
            Assert.notNull(user, "用户不存在");
            // 封装用户详情
            MyUserDetails myUserDetails = new MyUserDetails();
            BeanUtil.copyProperties(user, myUserDetails);
            // 获取用户角色
            Long userId = user.getId();
            LambdaQueryWrapper<UserRole> userRoleWrapper = new LambdaQueryWrapper<>();
            userRoleWrapper.eq(UserRole::getUserId, userId)
                    .eq(UserRole::getIsDeleted, 0);
            List<UserRole> userRoles = userRoleService.list(userRoleWrapper);
            // 获取 roleId 集合
            List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).toList();
            // 获取 role
            List<Role> roles = roleService.listByIds(roleIds);
            // 转换为 authorities
            List<MyAuthority> authorities = roles.stream().map(r -> {
                MyAuthority myAuthority = new MyAuthority();
                myAuthority.setRoleCode(r.getRoleCode());
                return myAuthority;
            }).toList();
            // 返回 userDetails
            myUserDetails.setAuthorities(authorities);
            return myUserDetails;
        } catch (Exception ex) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "获取用户详情失败");
        }
    }
}
