package com.flower.game.user.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 获取新的 accessToken
 *
 * @author F1ower
 * @since 2026-1-1
 */
@Data
public class TokenRefreshRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -541850656682387780L;

    /**
     * 刷新用 token
     */
    @NotBlank(message = "token 不能为空")
    private String refreshToken;
}
