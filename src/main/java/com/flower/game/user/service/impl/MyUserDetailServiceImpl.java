package com.flower.game.user.service.impl;

import com.flower.game.user.models.entity.MyUserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 自定义 security userDetailService 实现
 */
public class MyUserDetailServiceImpl implements UserDetailsService {


    /**
     * 根据名称加载 userDetail
     *
     * @param username 用户名
     * @return MyUserDetails 自定义 userDetail
     */
    @Override
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
