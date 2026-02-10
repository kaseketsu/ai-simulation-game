package com.flower.game.progress.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户灵材仓库库存信息
 *
 * @author F1ower
 * @since 2026-2-10
 */
@Data
public class SpiritualRepoInfoVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2734098338439535770L;

    /**
     * repo id
     */
    private Long id;

    /**
     * 稀有度
     */
    private Integer rarity;

    /**
     * 灵材名称
     */
    private String name;

    /**
     * 灵材 url
     */
    private String url;

    /**
     * 灵材数量
     */
    private Integer count;
}
