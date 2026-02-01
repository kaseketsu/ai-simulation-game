package com.flower.game.progress.service;

import com.flower.game.progress.model.dto.DailyInfoComputeRequest;
import common.annotations.ExceptionLog;
import common.constant.MarketConstant;
import common.manager.RedisManager;
import common.utils.MarketUtils;
import common.utils.ThreadLocalUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 处理游戏进程相关逻辑
 *
 * @author F1ower
 * @since 2026-1-31
 */
@Service
@Slf4j
public class GamePlayProgressService {

    @Resource
    private RedisManager redisManager;

    /**
     * 计算灵市波动比例、灵材总数限制
     *
     * @param request 计算请求
     */
    @ExceptionLog("计算每日数据失败")
    public void computeDailyRatio(DailyInfoComputeRequest request) {
        // 灵材市场今日比例 [0.7, 1.7]
        double waveRatio = MarketUtils.fetchWavePriceRatio();
        // 判断是否有 waveRatio
        if (redisManager.hasKey(MarketConstant.WAVE_RATIO)) {
            redisManager.removeKey(MarketConstant.WAVE_RATIO);
        }
        // 放入 threadLocal
        redisManager.addValue(MarketConstant.WAVE_RATIO, waveRatio);
        log.info("今日零市波动比例为: {}", waveRatio);
    }
}
