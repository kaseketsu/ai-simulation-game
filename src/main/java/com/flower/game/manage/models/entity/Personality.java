package com.flower.game.manage.models.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 个性类
 *
 * @author F1ower
 * @since 2026-2-24
 */
@Data
public class Personality implements Serializable {

    @Serial
    private static final long serialVersionUID = 7741009545215918223L;

    /**
     * 性格类型（数字枚举值）
     */
    private Integer temperament;

    /**
     * 说话风格（数字枚举值）
     */
    private Integer speechStyle;

    /**
     * 性格特质列表
     */
    private List<String> traits;
}
