package com.flower.game.user.models.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
public class MyAuthority implements GrantedAuthority {

    /**
     * 角色
     */
    private String role;

    /**
     * 获取角色
     *
     * @return 角色
     */
    @Override
    public String getAuthority() {
        return this.role;
    }
}
