package com.flower.game.dialogue.models.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.flower.game.dialogue.models.entity.BargainResp;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 灵膳还价对话响应类
 *
 * @author F1ower
 * @since 2026-3-8
 */
@Data
public class CultivatorDialogueResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -1627751920549049569L;

    /**
     * 姓名
     */
    private String name;

    /**
     * 道号
     */
    private String daoTitle;

    /**
     * 性别 (0-男,1-女)
     */
    private Byte gender;

    /**
     * 身份 (0-散修,1-内门,2-核心,3-长老)
     */
    private Byte status;

    /**
     * 地域
     */
    private Integer region;

    /**
     * 修士形象
     */
    private String url;

    /**
     * 还价响应
     */
    private BargainResp bargainResp;
}
