package com.flower.game.progress.controller;

import com.flower.game.progress.model.dto.PlayProgressQueryRequest;
import com.flower.game.progress.model.dto.PlayProgressSaveRequest;
import com.flower.game.progress.model.entity.PlayProgress;
import com.flower.game.progress.model.vo.PlayProgressVO;
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
    private IPlayProgressService playProgressService;

    /**
     * 用户游玩进度保存
     *
     * @param request 保存请求
     * @return 自定义响应
     */
    @PostMapping("save/playProgress")
    @ApiErrorCode(ErrorCode.SAVE_ERROR)
    public BaseResponse<String> savePlayProgress(@RequestBody PlayProgressSaveRequest request) {
        playProgressService.savePlayProgress(request);
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
        PlayProgressVO playProgressVO = playProgressService.queryPlayProgress(request);
        return ResultUtils.success(playProgressVO);
    }
    
}
