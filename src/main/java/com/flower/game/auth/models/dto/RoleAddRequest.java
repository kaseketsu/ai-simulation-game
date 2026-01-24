package com.flower.game.auth.models.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 添加角色
 *
 * @author F1ower
 * @since 2026-1-3
 */
@Data
public class RoleAddRequest implements Serializable {

    /**
     * 角色值
     */
    private String roleName;

    /**
     * 角色 code
     */
    private String roleCode;

    /**
     * 角色描述
     */
    private String roleDesc;
}
