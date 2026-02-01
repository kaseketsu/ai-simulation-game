package com.flower.game.progress.controller;

import com.flower.game.progress.model.dto.DailyInfoComputeRequest;
import com.flower.game.progress.model.dto.PlayProgressQueryRequest;
import com.flower.game.progress.model.dto.PlayProgressSaveRequest;
import com.flower.game.progress.model.entity.PlayProgress;
import com.flower.game.progress.model.vo.PlayProgressVO;
import com.flower.game.progress.service.GamePlayProgressService;
import com.flower.game.progress.service.IPlayProgressService;
import common.annotations.ApiErrorCode;
import common.baseEntities.BaseResponse;
import common.exceptions.ErrorCode;
import common.utils.ResultUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户游玩进度表 前端控制器
 * </p>
 *
 * @author F1ower
 * @since 2026-01-10
 */
@RestController
@RequestMapping("/playProgress")
public class PlayProgressController {

    @Resource
    private IPlayProgressService IPlayProgressService;

    @Resource
    private GamePlayProgressService gamePlayProgressService;

    /**
     * 用户游玩进度保存
     *
     * @param request 保存请求
     * @return 自定义响应
     */
    @PostMapping("save/playProgress")
    @ApiErrorCode(ErrorCode.SAVE_ERROR)
    public BaseResponse<String> savePlayProgress(@RequestBody PlayProgressSaveRequest request) {
        IPlayProgressService.savePlayProgress(request);
        return ResultUtils.success("游戏进度保存成功");
    }

    /**
     * 用户游玩进度查询
     *
     * @param request 查询请求
     * @return 游玩进度
     */
    @PostMapping("query/playProgress")
    @ApiErrorCode(ErrorCode.QUERY_ERROR)
    public BaseResponse<PlayProgressVO> queryPlayProgress(@RequestBody PlayProgressQueryRequest request) {
        PlayProgressVO playProgressVO = IPlayProgressService.queryPlayProgress(request);
        return ResultUtils.success(playProgressVO);
    }


    /**
     * 计算当天的必要信息
     *
     * @param request 计算请求
     * @return 自定义响应
     */
    @PostMapping("compute/dailyInfo")
    @ApiErrorCode(ErrorCode.COMPUTE_ERROR)
    public BaseResponse<String> computeDailyInfo(@RequestBody DailyInfoComputeRequest request) {
        // 计算当亲的信息
        gamePlayProgressService.computeDailyRatio(request);
        return ResultUtils.success("每日信息计算完毕");
    }
}
