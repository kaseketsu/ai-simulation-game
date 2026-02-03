package com.flower.game.market.service;

import cn.hutool.json.JSONUtil;
import com.flower.game.base.models.entity.SpiritualMaterialAllCat;
import com.flower.game.entrance.models.entity.SpiritualMaterialForRedis;
import com.flower.game.entrance.service.SpiritualMaterialLocalService;
import com.flower.game.market.models.dto.BuyMaterialRequest;
import com.flower.game.market.models.dto.SpiritualMaterialBaseRequest;
import common.annotations.ExceptionLog;
import common.constant.MarketConstant;
import common.exceptions.ErrorCode;
import common.manager.RedisManager;
import common.page.PageVO;
import common.utils.PageUtils;
import common.utils.ThrowUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 灵材采买相关服务
 *
 * @author F1ower
 * @since 2026-1-31
 */
@Service
@Slf4j
public class MarketService {

    @Value("${spring.spiritual.redis-key}")
    private String redisKey;

    @Resource
    private RedisManager redisManager;

    @Resource
    private SpiritualMaterialLocalService spiritualMaterialLocalService;

    /**
     * 根据前端传递 type 不同，展示不同 type 的所有，分页展示
     *
     * @param request 基础灵材查询请求
     */
    @ExceptionLog("获取基础灵材失败")
    public PageVO<SpiritualMaterialAllCat> fetchSpiritualMaterial(final SpiritualMaterialBaseRequest request) {
        // 校验参数
        int type = request.getType();
        ThrowUtils.throwIf(type < 0 || type > 6, ErrorCode.PARAM_ERROR, "灵材种类不存在");
        // 从 redis 拿对应种类的灵材
        String key = redisKey + type;
        boolean isExist = redisManager.hasKey(key);
        // 如果不存在则重新刷新
        if (!isExist) {
            // 初始化到 redis
            spiritualMaterialLocalService.init();
        }
        // 从 redis 取出对应的值
        String res = redisManager.getValue(key, String.class);
        // 转为对应实体
        SpiritualMaterialForRedis materialForRedis = JSONUtil.toBean(res, SpiritualMaterialForRedis.class);
        // 获取 List
        List<SpiritualMaterialAllCat> catList = materialForRedis.getCatList();
        // 获取当日波动
        Double ratio = redisManager.getValue(MarketConstant.WAVE_RATIO, Double.class);
        double waveRatios = ratio == null ? 1.0 : ratio;
        for (SpiritualMaterialAllCat c : catList) {
            // 重新计算价格
            Long price = Math.round(c.getNormalPrice() * 1.0 * (1.0 + waveRatios));
            c.setNormalPrice(price);
        }
        // 分页
        return PageUtils.buildPageVO(catList, request.getPageSize(), request.getCurrentPage());
    }

    /**
     * 购买灵材
     *
     * @param request 购买请求
     */
    public void buyMaterial(final BuyMaterialRequest request) {

    }
}
