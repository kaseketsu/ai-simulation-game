package com.flower.game.cultivator.models.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户类
 *
 * @author F1ower
 * @since 2026-2-24
 */
@Data
public class Customer implements Serializable {

    @Serial
    private static final long serialVersionUID = -5010047203799197881L;

    /**
     * 身份信息
     */
    private Identity identity;

    /**
     * 个性信息
     */
    private Personality personality;

    /**
     * 对话信息
     */
    private Dialogue dialogue;
}
