package com.flower.game.auth.models.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * 自定义 grandAuthority
 */
@Data
public class MyAuthority implements GrantedAuthority {

    /**
     * 角色
     */
    private String roleCode;

    /**
     * 获取角色
     *
     * @return 角色
     */
    @Override
    public String getAuthority() {
        return this.roleCode;
    }
}
