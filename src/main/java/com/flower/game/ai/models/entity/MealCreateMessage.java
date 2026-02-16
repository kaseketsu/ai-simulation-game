package com.flower.game.ai.models.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 灵膳创建消息
 *
 * @author F1ower
 * @since 2026-2-16
 */
@Data
public class MealCreateMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 8626509070146144892L;

    /**
     * 主食材
     */
    private String mainIngredient;

    /**
     * 副食材
     */
    private String sideIngredient;

    /**
     * 调味料
     */
    private String seasoning;

    /**
     * 稀有度（取最高）
     */
    private String rarity;
}
