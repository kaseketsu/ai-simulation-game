package com.flower.game.cultivator.models.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 对话类
 *
 * @author F1ower
 * @since 2026-2-24
 */
@Data
public class Dialogue implements Serializable {

    @Serial
    private static final long serialVersionUID = -6434052410174299021L;

    /**
     * 开场白
     */
    private String openingLine;
}
