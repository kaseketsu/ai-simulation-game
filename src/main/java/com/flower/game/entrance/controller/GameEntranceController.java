package com.flower.game.entrance.controller;

import com.flower.game.entrance.models.dto.GameInitRequest;
import com.flower.game.entrance.models.vo.GameInitStateVO;
import com.flower.game.entrance.service.GameEntranceService;
import common.annotations.ApiErrorCode;
import common.baseEntities.BaseResponse;
import common.exceptions.ErrorCode;
import common.utils.ResultUtils;
import common.utils.ThrowUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 游戏入口相关
 *
 * @author F1ower
 * @since 2026-1-11
 */
@RestController
@RequestMapping("/game/entrance")
public class GameEntranceController {

    @Resource
    private GameEntranceService gameEntranceService;

    /**
     * 游戏初始化
     *
     * @param initRequest 初始化请求
     * @return 初始化值
     */
    @PostMapping("/start")
    @ApiErrorCode(ErrorCode.GAME_INIT_ERROR)
    public BaseResponse<GameInitStateVO> gameStart(@RequestBody GameInitRequest initRequest) {
        // 校验初始化请求
        ThrowUtils.throwIf(Objects.isNull(initRequest), ErrorCode.GAME_INIT_ERROR);
        // 获取游戏初始化状态
        GameInitStateVO gameInitStateVO = gameEntranceService.gameStart(initRequest);
        // 返回游戏初始化状态
        return ResultUtils.success(gameInitStateVO);
    }
}
