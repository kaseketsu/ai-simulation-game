package com.flower.game.dish.models.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 研制灵膳材料
 */
@Data
public class MaterialVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3636694633754986439L;

    /**
     * 灵材名称
     */
    private String name;

    /**
     * 图片 url
     */
    private String url;

    /**
     * 灵材描述
     */
    private String description;
}
