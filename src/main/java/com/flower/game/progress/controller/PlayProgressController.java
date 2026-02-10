package com.flower.game.progress.controller;

import com.flower.game.progress.model.dto.DailyInfoComputeRequest;
import com.flower.game.progress.model.dto.PlayProgressQueryRequest;
import com.flower.game.progress.model.dto.PlayProgressSaveRequest;
import com.flower.game.progress.model.dto.SpiritualRepoQueryRequest;
import com.flower.game.progress.model.entity.PlayProgress;
import com.flower.game.progress.model.vo.PlayProgressVO;
import com.flower.game.progress.model.vo.SpiritualRepoInfoVO;
import com.flower.game.progress.service.GamePlayProgressService;
import com.flower.game.progress.service.IPlayProgressService;
import common.annotations.ApiErrorCode;
import common.baseEntities.BaseResponse;
import common.exceptions.ErrorCode;
import common.page.PageVO;
import common.utils.ResultUtils;
import common.utils.ThrowUtils;
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

    /**
     * 查询灵材仓库
     *
     * @param queryRequest 查询请求
     * @return 灵材仓库信息
     */
    @PostMapping("list/SpiritualRepo")
    @ApiErrorCode(ErrorCode.SPIRITUAL_REPO_QUERY_ERROR)
    public BaseResponse<PageVO<SpiritualRepoInfoVO>> listSpiritualRepoByPage(@RequestBody SpiritualRepoQueryRequest queryRequest) {
        // 校验参数
        ThrowUtils.throwIf(queryRequest == null, ErrorCode.PARAM_ERROR, "灵材仓库查询请求不能为空");
        PageVO<SpiritualRepoInfoVO> spiritualRepoInfoVOPageVO = gamePlayProgressService.listSpiritualRepoByPage(queryRequest);
        return ResultUtils.success(spiritualRepoInfoVOPageVO);
    }
}
