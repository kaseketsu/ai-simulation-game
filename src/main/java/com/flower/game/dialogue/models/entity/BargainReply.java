package com.flower.game.dialogue.models.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 修士谈价回复结果实体类
 * 存储AI生成的谈价对话、价格诉求及谈价状态
 *
 * @author F1ower
 * @since 2026-2-25
 */
@Data
public class BargainReply implements Serializable {

    @Serial
    private static final long serialVersionUID = -7894561230123456789L;

    /**
     * 修士谈价回复文本
     * 要求：修仙风格，长度15-40字，适配游戏UI气泡展示
     * 例："掌柜莫要欺我散修！这赤焰灵肉羹百文太贵，八十灵石可否？"
     */
    private String bargainReply;

    /**
     * 本轮修士期望价格（单位：灵石）
     * null表示无价格诉求（直接同意当前价格/拒绝谈价）
     * 非null时需在[minAcceptPrice, maxAcceptPrice]区间内
     */
    private Long expectedPrice;

    /**
     * 是否接受掌柜当前报价
     * true=接受，false=不接受（仍想砍价）
     */
    private Boolean isAcceptCurrentPrice;

    /**
     * 是否结束谈价
     * true=结束（次数用尽/成交/拒绝购买），false=继续谈价
     */
    private Boolean isEndBargain;
}