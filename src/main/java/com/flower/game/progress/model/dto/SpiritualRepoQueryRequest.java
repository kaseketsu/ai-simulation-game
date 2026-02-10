package com.flower.game.progress.model.dto;

import common.page.PageRequest;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 请求查询用户灵材仓库信息
 *
 * @author F1ower
 * @since 2026-2-10
 */
@Data
public class SpiritualRepoQueryRequest extends PageRequest implements Serializable{

    @Serial
    private static final long serialVersionUID = 7179745396600370129L;

    /**
     * 用户 id
     */
    private Long userId;
}
