package com.flower.game.cultivator.models.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 修士提示信息DTO
 * 用于封装生成修士相关内容的输入变量
 *
 * @author F1ower
 * @since 2026-2-25
 */
@Data
public class CultivatorImageCreateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 7895421036789876543L; // 随机生成的序列化版本号

    /**
     * 修士姓名
     */
    private String name;

    /**
     * 性别 // 0=男性，1=女性
     */
    private Integer gender;

    /**
     * 身份 // 0=散修，1=内门弟子，2=核心弟子，3=宗门长老
     */
    private Integer status;

    /**
     * 地域风格 // 0=玄洲，1=沧澜
     */
    private Integer region;

    /**
     * 核心性情 // 0=沉稳，1=高傲，2=节俭，3=热忱，4=凌厉，5=神秘
     */
    private Integer temperament;

    /**
     * 性格特质列表（支持traits[0]、traits[1]取值）
     */
    private List<String> traits;
}