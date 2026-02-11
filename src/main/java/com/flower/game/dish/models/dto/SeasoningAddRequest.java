package com.flower.game.dish.models.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用于后端自己加调味料
 */
@Data
public class SeasoningAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 2889873516225251539L;

    /**
     * 调味料名称
     */
    private String name;

    /**
     * 调味料 url
     */
    private String url;

    /**
     * 调味料描述
     */
    private String desc;
}
