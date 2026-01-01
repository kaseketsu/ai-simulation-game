package com.flower.game.user.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录请求
 *
 * @author F1ower
 * @since 2026-1-1
 */
@Data
public class UserLoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 4171661454576166071L;

    /**
     * 用户账号
     */
    @NotBlank(message = "用户账号不能为空")
    private String userAccount;

    /**
     * 用户密码
     */
    @NotBlank(message = "用户密码不能为空")
    private String userPassword;
}
