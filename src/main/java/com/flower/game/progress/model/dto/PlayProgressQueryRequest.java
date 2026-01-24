package com.flower.game.progress.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户游玩进度查询请求
 */
@Data
public class PlayProgressQueryRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 5697486888906431105L;

    /**
     * 用户 id
     */
    private Long userId;
}
