package com.flower.game.user.models.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户下线请求
 *
 * @author F1ower
 * @since 2026-1-1
 */
@Data
public class UserLogOutRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 5812105090572004183L;

    /**
     * 用户账号
     */
    private String userAccount;
}
