package com.flower.game.cultivator.models.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 身份信息
 *
 * @author F1ower
 * @since 2026-2-24
 */
@Data
public class Identity implements Serializable {

    @Serial
    private static final long serialVersionUID = -4790198442223564510L;

    /**
     * 姓名
     */
    private String name;

    /**
     * 职称 /头衔（可为null）
     */
    private String daoTitle;

    /**
     * 性别（数字类型，如 0-未知、1-男、2-女）
     */
    private Integer gender;

    /**
     * 状态（数字类型，如 0-禁用、1-正常）
     */
    private Integer status;

    /**
     * 地区（数字枚举值）
     */
    private Integer region;

    /**
     * 店铺类型（数字枚举值）
     */
    private Integer storeType;
}
