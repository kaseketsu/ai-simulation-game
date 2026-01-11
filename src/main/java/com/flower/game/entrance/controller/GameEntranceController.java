package com.flower.game.entrance.controller;

import com.flower.game.entrance.models.dto.GameInitRequest;
import com.flower.game.entrance.models.vo.GameInitStateVO;
import common.annotations.ApiErrorCode;
import common.baseEntities.BaseResponse;
import common.exceptions.ErrorCode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 游戏入口相关
 *
 * @author F1ower
 * @since 2026-1-11
 */
@RestController
@RequestMapping("/game/entrance")
public class GameEntranceController {

    /**
     * 游戏初始化
     *
     * @param initRequest 初始化请求
     * @return 初始化值
     */
    @PostMapping("/start")
    @ApiErrorCode(ErrorCode.GAME_INIT_ERROR)
    public BaseResponse<GameInitStateVO> gameStart(@RequestBody GameInitRequest initRequest) {
        return null;
    }
}
