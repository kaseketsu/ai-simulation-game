package com.flower.game.user.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户注册请求
 *
 * @author F1ower
 * @since 2026-1-2
 */
@Data
public class UserRegisterRequest {

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户账号
     */
    @NotBlank(message = "账号不能为空")
    @Size(min = 8, message = "账号长度不能小于 8 位")
    private String userAccount;

    /**
     * 用户密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, message = "密码长度不能小于 8 位")
    private String userPassword;

    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    @Size(min = 8, message = "确认密码长度不能小于 8 位")
    private String checkPassword;
}
