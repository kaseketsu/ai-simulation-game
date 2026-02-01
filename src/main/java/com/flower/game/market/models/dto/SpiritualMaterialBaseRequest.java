package com.flower.game.market.models.dto;

import common.page.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 基础灵材请求
 *
 * @author F1ower
 * @since 2026-2-1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SpiritualMaterialBaseRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 2582990718312445697L;

    /**
     * 灵材种类
     */
    private int type;
}
