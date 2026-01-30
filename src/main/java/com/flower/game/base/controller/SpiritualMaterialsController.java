package com.flower.game.base.controller;

import com.flower.game.base.models.dto.BaseSpritualMaterialsAddReq;
import com.flower.game.base.service.BaseLibService;

import common.annotations.ApiErrorCode;
import common.baseEntities.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import common.exceptions.ErrorCode;
import common.utils.ResultUtils;
import io.lettuce.core.api.async.BaseRedisAsyncCommands;

import java.util.List;

/**
 * 灵材控制器
 * 
 * @author Flower
 * @since 2026-01-30
 */
@RestController
@RequestMapping("/spiritualMaterials")
@RequiredArgsConstructor
public class SpiritualMaterialsController {

    private final BaseLibService baseLibService;

    /**
     * 批量添加基础灵材
     *
     * @param reqList 添加请求参数列表
     */
    @PostMapping("/batchAdd")
    @ApiErrorCode(ErrorCode.BASE_LIB_ERROR)
    public BaseResponse<String> batchAddBaseSpiritualMaterials(@RequestBody List<BaseSpritualMaterialsAddReq> reqList) {
        baseLibService.batchAddBaseSpiritualMaterials(reqList);
        return ResultUtils.success("批量添加基础灵材成功");
    }
}