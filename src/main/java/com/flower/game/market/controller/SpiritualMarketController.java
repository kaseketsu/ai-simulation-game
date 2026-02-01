package com.flower.game.market.controller;

import cn.hutool.core.lang.Assert;
import com.flower.game.base.models.entity.SpiritualMaterialAllCat;
import com.flower.game.market.models.dto.SpiritualMaterialBaseRequest;
import com.flower.game.market.service.MarketService;
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
 * 灵材采买控制器
 *
 * @author F1ower
 * @since 2026-2-1
 */
@RestController
@RequestMapping("/market")
public class SpiritualMarketController {

    @Resource
    private MarketService marketService;

    /**
     * 查询基础灵材
     *
     * @param request 查询请求
     * @return 分页数据
     */
    @PostMapping("/query/base/spiritualMaterial")
    @ApiErrorCode(ErrorCode.SPIRITUAL_MATERIAL_QUERY_ERROR)
    public BaseResponse<PageVO<SpiritualMaterialAllCat>> queryBaseSpiritualMaterials(@RequestBody SpiritualMaterialBaseRequest request) {
        // 校验参数
        ThrowUtils.throwIf(request == null, ErrorCode.PARAM_ERROR, "灵材查询请求不能为空");
        // 获取分页数据
        PageVO<SpiritualMaterialAllCat> pageVO = marketService.fetchSpiritualMaterial(request);
        return ResultUtils.success(pageVO);
    }
}
