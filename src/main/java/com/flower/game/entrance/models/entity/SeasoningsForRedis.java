package com.flower.game.entrance.models.entity;

import com.flower.game.dish.models.entity.SpiritualSeasoningBase;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 调味料实体类
 */
@Data
public class SeasoningsForRedis implements Serializable {

    @Serial
    private static final long serialVersionUID = 9153737881763546183L;

    /**
     * 基础调味料集合
     */
    private List<SpiritualSeasoningBase> seasoningBaseList;
}
