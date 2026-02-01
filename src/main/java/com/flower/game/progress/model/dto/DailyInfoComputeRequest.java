package com.flower.game.progress.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 日常信息计算请求
 *
 * @author F1ower
 * @since 2026-2-1
 */
@Data
public class DailyInfoComputeRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 8833134263466889915L;

    /**
     * 当前店铺开设天数
     */
    private int openDays;
}
