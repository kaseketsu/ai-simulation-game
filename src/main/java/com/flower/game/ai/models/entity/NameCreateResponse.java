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
    private String name;

    /**
     * 普通品阶 url
     */
    private String url;

    /**
     * 普通品阶描述
     */
    private String description;

    /**
     * 普通品阶价格
     */
    private Long price;
}
