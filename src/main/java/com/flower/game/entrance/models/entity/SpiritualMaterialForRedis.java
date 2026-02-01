package com.flower.game.entrance.models.entity;

import com.flower.game.base.models.entity.SpiritualMaterialAllCat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 为了存入 redis 创建的实体类
 *
 * @author F1ower
 * @since 2026-2-1
 */
@Data
public class SpiritualMaterialForRedis implements Serializable {

    @Serial
    private static final long serialVersionUID = 395531660604369860L;

    /**
     * 灵材封装实体类
     */
    List<SpiritualMaterialAllCat> catList;
}
