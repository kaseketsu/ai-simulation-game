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

    private String roleName;

    private String roleCode;
}
