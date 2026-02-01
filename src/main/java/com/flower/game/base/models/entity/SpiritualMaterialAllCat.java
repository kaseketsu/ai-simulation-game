package com.flower.game.base.models.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 每种灵材全品阶的数据都放在内部
 * normal -> rare -> superRare -> mythical
 *
 * @author F1ower
 * @since 2026-1-31
 */
@Data
public class SpiritualMaterialAllCat implements Serializable {

    @Serial
    private static final long serialVersionUID = 3423673238646583711L;

    /**
     * 种类
     */
    private int type;

    /**
     * 普通品阶名称
     */
    private String normalName;

    /**
     * 普通品阶 url
     */
    private String normalUrl;

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
}
