package com.flower.game.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flower.game.auth.models.entity.MyAuthority;
import com.flower.game.auth.models.entity.Role;
import com.flower.game.auth.service.IRoleService;
import com.flower.game.user.dao.UserMapper;
import com.flower.game.user.models.entity.*;
import com.flower.game.user.service.*;
import common.annotations.ExceptionLog;
import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 自定义 security userDetailService 实现
 */
@Slf4j
@Service
public class MyUserDetailServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private IUserRoleService userRoleService;

    @Resource
    private IRoleService roleService;

    /**
     * 根据名称加载 userDetail
     *
     * @param userAccount 用户账号
     * @return MyUserDetails 自定义 userDetail
     */
    @ExceptionLog("获取用户详情失败")
    @Override
    public MyUserDetails loadUserByUsername(@NonNull final String userAccount) throws UsernameNotFoundException {
        Assert.notBlank(userAccount, "用户账号不能为空");
        User user = userMapper.selectByUserAccount(userAccount);
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
    }
}
