package com.flower.game.ai.models.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * ai 对于创建名称的返回结果实体
 *
 * @author F1ower
 * @since 2026-2-16
 */
@Data
public class NameCreateResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1696147815850790052L;

    /**
     * 灵材类型 (0 - 灵谷, 1 - 灵蔬, 2 - 灵肉, 3 - 灵茶, 4 - 灵果, 5 - 灵酿)
     */
    private Integer type;

    /**
     * 店铺类型 (0 - 固本类, 1 - 淬灵类)
     */
    private Integer storeType;

    /**
     * 普通品阶名称
     */
    private String normalName;

    /**
     * 普通品阶 url
     */
    private String normalUrl;

    /**
     * 普通品阶描述
     */
    private String normalDescription;

    /**
     * 普通品阶价格
     */
    private Long normalPrice;

    /**
     * 稀有品阶名称
     */
    private String rareName;

    /**
     * 稀有品阶 url
     */
    private String rareUrl;

    /**
     * 稀有品阶价格
     */
    private Long rarePrice;

    /**
     * 稀有描述
     */
    private String rareDescription;

    /**
     * 超稀有品阶名称
     */
    private String superRareName;

    /**
     * 超稀有品阶 url
     */
    private String superRareUrl;

    /**
     * 超稀有品阶价格
     */
    private Long superRarePrice;

    /**
     * 传世描述
     */
    private String superRareDescription;

    /**
     * 超超稀有品阶名称
     */
    private String mythicalName;

    /**
     * 超超稀有品阶 url
     */
    private String mythicalUrl;

    /**
     * 超超稀有品阶价格
     */
    private Long mythicalPrice;

    /**
     * 神话品阶描述
     */
    private String mythicalDescription;
}
