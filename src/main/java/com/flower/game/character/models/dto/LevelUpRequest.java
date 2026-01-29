package com.flower.game.character.models.dto;

import lombok.Data;

import java.io.Serializable;

import java.io.Serial;


/**
 * 等级升级请求
 *
 * @author F1ower
 * @since 2026-1-11
 */
@Data
public class LevelUpRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 等级升级次数
     */
    private Integer levelUpCount;


    /**
     * 感知能力升级次数
     */
    private Integer senseUp;

    /**
     * 口才升级次数
     */
    private Integer speakingSkillUp;

    /**
     * 厨艺升级次数
     */
    private Integer cookingSkillUp;
}
