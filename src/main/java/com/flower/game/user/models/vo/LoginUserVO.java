package com.flower.game.user.models.vo;

import lombok.Data;

/**
 * 登录用户信息 vo 类
 *
 * @author F1ower
 * @since 2026-1-26
 */
@Data
public class LoginUserVO {

    /**
     * 用户 id
     */
    Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户角色
     */
    private String userRole;

    /**
     * 用户状态, 0 - 正常, 1 - 禁用, 2 - 冻结
     */
    private Integer userState;
}
