package com.flower.game.dialogue.models.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 修士基础信息
 *
 * @author F1ower
 * @since 2026-2-25
 */
@Data
public class CultivationInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = -3624783560687657972L;

    /**
     * 修士姓名（如：李云曦、王二狗）
     */
    private String name;

    /**
     * 道号（核心弟子/长老有值，散修/内门弟子为null；如：赤霞真人、忘尘居士）
     */
    private String daoTitle;

    /**
     * 性别
     * 0 - 男性
     * 1 - 女性
     */
    private Integer gender;

    /**
     * 身份状态
     * 0 - 散修
     * 1 - 内门弟子
     * 2 - 核心弟子
     * 3 - 宗门长老
     */
    private Integer status;

    /**
     * 性情
     * 0 - calm(沉稳)
     * 1 - proud(高傲)
     * 2 - frugal(节俭)
     * 3 - warm(热忱)
     * 4 - sharp(凌厉)
     * 5 - mysterious(神秘)
     */
    private Integer temperament;

    /**
     * 说话风格
     * 0 - plain(平实)
     * 1 - elegant(文雅)
     * 2 - aloof(冷淡)
     * 3 - bold(豪放)
     */
    private Integer speechStyle;

    /**
     * 地域风格
     * 0 - 玄洲
     * 1 - 沧澜
     */
    private Integer region;

}