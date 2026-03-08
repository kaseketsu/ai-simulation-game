package com.flower.game.dialogue.models.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 修士灵膳还价请求类
 *
 * @author F1ower
 * @since 2026-3-8
 */
@Data
public class CultivatorDialogueRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 8043365852562532971L;

    /**
     * 修士 id
     */
    private Long cultivatorId;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 已谈价次数
     */
    private Integer bargainTimes;

    /**
     * 当前掌柜报价
     */
    private Long currentPrice;
}
