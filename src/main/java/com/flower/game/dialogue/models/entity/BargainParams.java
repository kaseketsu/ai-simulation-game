package com.flower.game.dialogue.models.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 修士谈价参数实体类
 * 存储谈价功能所需的核心数值与业务参数
 *
 * @author F1ower
 * @since 2026-2-25
 */
@Data
public class BargainParams implements Serializable {

    @Serial
    private static final long serialVersionUID = 8975421634578901234L;

    /**
     * 当前谈价轮次（从1开始计数）
     * 例：散修首次谈价为1，第二次为2
     */
    private Integer currentBargainRound;

    /**
     * 该修士总谈价次数（由身份决定上限）
     * 例：散修0-2次，内门弟子1-4次
     */
    private Integer totalBargainTimes;

    /**
     * 掌柜当前报价（单位：灵石）
     * 采用Long避免高品阶灵膳价格溢出
     */
    private Long currentPrice;

    /**
     * 修士可接受的最低价格（单位：灵石）
     * 例：散修=基础价×0.5，宗门长老=基础价×1.0
     */
    private Long minAcceptPrice;

    /**
     * 修士可接受的最高价格（单位：灵石）
     * 例：散修=基础价×1.1，宗门长老=基础价×2.0
     */
    private Long maxAcceptPrice;
}