package com.flower.game.progress.controller;

import com.flower.game.progress.model.dto.PlayProgressQueryRequest;
import com.flower.game.progress.model.dto.PlayProgressSaveRequest;
import com.flower.game.progress.model.entity.PlayProgress;
import com.flower.game.progress.model.vo.PlayProgressVO;
import common.baseEntities.BaseResponse;
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

    /**
     * 用户游玩进度保存
     *
     * @param request 保存请求
     * @return 自定义响应
     */
    @PostMapping("save/playProgress")
    public BaseResponse<String> savePlayProgress(@RequestBody PlayProgressSaveRequest request) {
        return null;
    }

    /**
     * 用户游玩进度查询
     *
     * @param request 查询请求
     * @return 游玩进度
     */
    @PostMapping("query/playProgress")
    public BaseResponse<PlayProgressVO> queryPlayProgress(@RequestBody PlayProgressQueryRequest request) {
        return null;
    }

}
