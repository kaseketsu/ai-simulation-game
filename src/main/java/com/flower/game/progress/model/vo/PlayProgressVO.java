package com.flower.game.progress.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;

/**
 * 用户游玩进度展示
 *
 * @author F1ower
 * @since 2026-1-10
 */
@Data
public class PlayProgressVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6358330631361669832L;

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

    /**
     * 用户名
     */
    private String userName;

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
     * 用户 id
     */
    private Long userId;
}
