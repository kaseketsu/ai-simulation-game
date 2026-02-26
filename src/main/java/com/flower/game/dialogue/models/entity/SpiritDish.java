package com.flower.game.dialogue.models.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 灵珊
 *
 * @author F1ower
 * @since 2026-2-26
 */
@Data
public class SpiritDish implements Serializable {

    @Serial
    private static final long serialVersionUID = 6801029762833041414L;

    /**
     * 灵膳名称
     */
    private String name;

    /**
     * 灵膳价格
     */
    private Long price;

    /**
     * 灵膳描述
     */
    private String description;
}
