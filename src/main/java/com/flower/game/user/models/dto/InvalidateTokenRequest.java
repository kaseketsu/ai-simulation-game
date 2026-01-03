package com.flower.game.user.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 失效 token 请求
 *
 * @author F1ower
 * @since 2026-1-1
 */
@Data
public class InvalidateTokenRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 7440944228397403804L;

    /**
     * 用户账号
     */
    private String userAccount;
}
