package com.flower.game.entrance.models.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 游戏初始化请求
 *
 * @author F1ower
 * @since 2026-1-11
 */
@Data
public class GameInitRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1995484821665225885L;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户角色
     */
    private String userRole;

    /**
     * 0 - 饮品, 1 - 主食
     */
    private String storeType;

    /**
     * 商店名称
     */
    private String storeName;

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
}
