package com.flower.game.manage.models.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 培养创建请求类
 *
 * @author F1ower
 * @since 2026-2-24
 */
@Data
public class CultivationCreateRequest implements Serializable {


    @Serial
    private static final long serialVersionUID = 8852007634126029331L; // 随机生成的序列化版本号

    /**
     * 地区（数字枚举值）
     */
    private Integer region;

    /**
     * 店铺类型（数字枚举值）
     */
    private Integer storeType;

    /**
     * 批量大小（数字）
     */
    private Integer batchSize;

    /**
     * 名称
     */
    private String name;

    /**
     * 性别（数字枚举值，如 0-男、1-女）
     */
    private Integer gender;

    /**
     * 状态（数字枚举值，如 0-散修）
     */
    private Integer status;

    /**
     * 性格类型（数字枚举值）
     */
    private Integer temperament;

    /**
     * 性格特质列表
     */
    private List<String> traits;
}