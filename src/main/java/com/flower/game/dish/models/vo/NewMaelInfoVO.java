package com.flower.game.dish.models.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 新生成的灵膳信息
 *
 * @author F1ower
 * @since 2026-2-16
 */
@Data
public class NewMaelInfoVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3295245114680888923L;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 图片 ur;
     */
    private String url;
}
