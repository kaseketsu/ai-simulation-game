package com.flower.game.ai.models.entity;

import lombok.Data;

/**
 * 灵膳创建消息
 *
 * @author F1ower
 * @since 2026-2-16
 */
@Data
public class MealCreateMessage {

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
}
