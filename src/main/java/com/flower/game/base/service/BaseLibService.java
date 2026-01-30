package com.flower.game.base.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flower.game.base.models.dto.BaseSpritualMaterialsAddReq;
import com.flower.game.base.models.entity.SpiritualMaterialsBase;

import cn.hutool.core.bean.BeanUtil;
import common.annotations.ExceptionLog;
import common.exceptions.ErrorCode;
import common.utils.ParamsCheckUtils;
import common.utils.ThrowUtils;
import jakarta.annotation.Resource;

/**
 * 基础库服务
 * 
 * @author Flower
 * @since 2026-01-30
 */
@Service
public class BaseLibService {

    @Resource
    private ISpiritualMaterialsBaseService spiritualMaterialsBaseService;

    /**
     * 添加基础灵材
     *
     * @param req 添加请求参数
     */
    @ExceptionLog("添加基础灵材失败")
    public void addBaseSpiritualMaterials(final BaseSpritualMaterialsAddReq req) {
        // 校验参数
        ParamsCheckUtils.checkObj(req);
        // 根据 name 查看是否已存在
        LambdaQueryWrapper<SpiritualMaterialsBase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SpiritualMaterialsBase::getName, req.getName())
                .eq(SpiritualMaterialsBase::getIsDeleted, 0);
        long count = spiritualMaterialsBaseService.count(wrapper);
        ThrowUtils.throwIf(count > 0, ErrorCode.ALREADY_EXIST_ERROR, "灵材已存在");
        // 添加入库
        SpiritualMaterialsBase materials = BeanUtil.copyProperties(req, SpiritualMaterialsBase.class);
        spiritualMaterialsBaseService.saveOrUpdate(materials);
    }

    /**
     * 批量添加基础灵材
     *
     * @param reqList 添加请求参数列表
     */
    @ExceptionLog("批量添加基础灵材失败")
    public void batchAddBaseSpiritualMaterials(final List<BaseSpritualMaterialsAddReq> reqList) {
        // 校验参数
        ParamsCheckUtils.checkObj(reqList);
        // 转换为灵材实体列表
        List<SpiritualMaterialsBase> materialsList = reqList.stream()
                .map(this::convertToSpiritualMaterialsBase)
                .toList();
        // 批量添加
        spiritualMaterialsBaseService.saveOrUpdateBatch(materialsList);
    }

    /**
     * BaseSpritualMaterialsAddReq 转换为 SpiritualMaterialsBase
     *
     * @param req 添加请求参数
     * @return 灵材实体
     */
    private SpiritualMaterialsBase convertToSpiritualMaterialsBase(BaseSpritualMaterialsAddReq req) {
        return BeanUtil.copyProperties(req, SpiritualMaterialsBase.class);
    }
}
