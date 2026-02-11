package com.flower.game.dish.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.flower.game.base.models.entity.SpiritualMaterialsBase;
import com.flower.game.dish.models.dto.SeasoningBatchAddRequest;
import com.flower.game.dish.models.entity.SpiritualSeasoningBase;
import common.annotations.ExceptionLog;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 灵膳相关服务
 */
@Service
@Slf4j
public class DishService {

    @Resource
    private ISpiritualSeasoningBaseService iSpiritualSeasoningBaseService;

    /**
     * 批量添加灵膳
     *
     * @param request 添加请求
     */
    @ExceptionLog("批量添加灵膳失败")
    public void addSeasoningByBatch(SeasoningBatchAddRequest request) {
        // 获取请求
        List<SeasoningBatchAddRequest> requestList = request.getRequestList();
        // 判断参数
        if (CollUtil.isNotEmpty(requestList)) {
            return;
        }
        // 批量转化
        List<SpiritualSeasoningBase> bases = requestList.stream().map(r -> {
            SpiritualSeasoningBase spiritualSeasoningBase = new SpiritualSeasoningBase();
            BeanUtil.copyProperties(r, spiritualSeasoningBase);
            return spiritualSeasoningBase;
        }).toList();
        // 存入苦衷
        iSpiritualSeasoningBaseService.saveBatch(bases);
    }
}
