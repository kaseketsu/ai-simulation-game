package com.flower.game.user.models.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

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
     * 权限
     */
    private List<String> permissionCodes;

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
