package com.flower.game.dish.models.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 批量添加调味料
 *
 * @author F1ower
 * @since 2026-2-11
 */
@Data
public class SeasoningBatchAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 5419795844168781812L;

    /**
     * 批量添加请求
     */
    private List<SeasoningBatchAddRequest> requestList;
}
