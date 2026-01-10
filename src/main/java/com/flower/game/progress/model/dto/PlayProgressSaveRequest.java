package com.flower.game.progress.model.dto;

import lombok.Data;

import java.time.LocalTime;

/**
 * 保存游玩进度
 */
@Data
public class PlayProgressSaveRequest {

    /**
     * 用户 id
     */
    private Long userId;

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
