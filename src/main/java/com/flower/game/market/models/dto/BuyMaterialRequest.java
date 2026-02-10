package com.flower.game.market.models.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 灵材购买请求
 *
 * @author F1ower
 * @since 2026-2-3
 */
@Data
public class BuyMaterialRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 5910932796473453249L;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 灵材名称
     */
    private String name;

    /**
     * 购买数量
     */
    private Integer count;

    /**
     * 价格
     */
    private Long price;
}
