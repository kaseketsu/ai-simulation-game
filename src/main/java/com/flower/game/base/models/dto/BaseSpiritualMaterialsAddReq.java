package com.flower.game.base.models.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 基础灵材添加请求参数
 * 
 * @author Flower
 * @since 2026-01-30
 */
@Data
public class BaseSpiritualMaterialsAddReq {

     /**
     * 灵材名称
     */
    private String name;

    /**
     * 灵材类型 (0 - 灵谷, 1 - 灵蔬, 2 - 灵肉, 3 - 灵茶, 4 - 灵果, 5 - 灵酿)
     */
    private Integer type;

    /**
     * 稀有度 (1 - 普通, 2 - 稀有, 3 - 传世, 4 - 至尊)
     */
    private Integer rarity;

    /**
     * 灵材图片 url
     */

    private String url;

    /**
     * 灵材价格
     */
    private BigDecimal price;

    /**
     * 店铺类型 (0 - 固本类, 1 - 淬灵类)
     */
    private Integer storeType;

    /**
     * 灵材品阶 (1 - 低阶, 2 - 中阶, 3 - 高阶)
     */
    private Integer materialGrade;

}