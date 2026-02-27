package com.flower.game.cultivator.models.dto;

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
}