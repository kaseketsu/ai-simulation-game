package com.flower.game.entrance.models.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;

/**
 * 游戏创建后的初始状态
 *
 * @author F1ower
 * @since 2026-1-11
 */
@Data
public class GameInitStateVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1100295394376085027L;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 0 - 饮品, 1 - 主食
     */
    private String storeType;

    /**
     * 眼光
     */
    private Integer sense;

    /**
     * 口才
     */
    private Integer speakingSkill;

    /**
     * 厨艺
     */
    private Integer cookingSkill;

    /**
     * 经营天数
     */
    private Integer openDays;

    /**
     * 营业时段
     */
    private LocalTime timePeriod;

    /**
     * 营业额
     */
    private Long earnedMoney;

    /**
     * 店铺等级
     */
    private Integer storeLevel;
}
