package com.flower.game.user.models.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 自定义 userDetail
 */
@Data
@RequiredArgsConstructor
public class MyUserDetails implements UserDetails {


    /**
     * 权限
     */
    private List<MyAuthority> authorities;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 账号状态
     */
    private Integer userState;


    /**
     * 获取角色集合和权限集合
     *
     * @return 权限稽核
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    /**
     * 获取密码
     *
     * @return 密码
     */
    @Override
    public String getPassword() {
        return this.userPassword;
    }

    /**
     * 获取用户账号
     *
     * @return 用户名
     */
    @Override
    public String getUsername() {
        return this.userAccount;
    }

    /**
     * 查看账号是某过期
     *
     * @return 是否过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return this.userState != 0;
    }

    /**
     * 查看账号是否上锁
     *
     * @return 是否上锁
     */
    @Override
    public boolean isAccountNonLocked() {
        return this.userState != 0;
    }

    /**
     * 查看凭证是否过期（默认）
     *
     * @return 是否过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**
     * 账号是否可用
     *
     * @return 是否可用
     */
    @Override
    public boolean isEnabled() {
        return this.userState == 0;
    }
}
