package com.flower.game.dish.controller;

import com.flower.game.dish.models.dto.SeasoningBatchAddRequest;
import com.flower.game.dish.service.DishService;
import common.baseEntities.BaseResponse;
import common.utils.ParamsCheckUtils;
import common.utils.ResultUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 灵膳制作控制器
 *
 * @author F1ower
 * @since 2026-2-11
 */
@RestController
@RequestMapping("/dish")
public class DishMakeController {

    @Resource
    private DishService dishService;

    /**
     * 批量添加灵膳
     *
     * @param request 添加请求
     * @return 自定义响应
     */
    @PostMapping("/add/seasoning/batch")
    public BaseResponse<String> addSeasoningByBatch(@RequestBody SeasoningBatchAddRequest request) {
        // 校验参数
        ParamsCheckUtils.checkObj(request);
        dishService.addSeasoningByBatch(request);
        return ResultUtils.success("灵材批量添加成功");
    }
}
