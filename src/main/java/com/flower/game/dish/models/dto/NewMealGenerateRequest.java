package com.flower.game.dish.models.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 新灵膳生成请求
 *
 * @author F1ower
 * @since 2026-2-16
 */
@Data
public class NewMealGenerateRequest implements Serializable {

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 主食材 id
     */
    private Long mainId;

    /**
     * 主食材
     */
    private String mainIngredient;

    /**
     * 主食材描述
     */
    private String mainIngredientDescription;

    /**
     * 主食材稀有度
     */
    private Integer mainIngredientRarity;

    /**
     * 主食材价格
     */
    private Long mainIngredientPrice;

    /**
     * 主食材类型
     */
    private Integer mainIngredientType;

    /**
     * 辅食材 id
     */
    private Long sideId;

    /**
     * 副食材
     */
    private String sideIngredient;

    /**
     * 辅助食材价格
     */
    private Long sideIngredientPrice;

    /**
     * 副食材稀有度
     */
    private Integer sideIngredientRarity;

    /**
     * 主食材描述
     */
    private String sideIngredientDescription;

    /**
     * 附属食材类型
     */
    private Integer sideIngredientType;

    /**
     * 调味料
     */
    private String seasoning;

    /**
     * 主食材描述
     */
    private String seasoningDescription;

}
