package com.flower.game.progress.service;

import common.annotations.ExceptionLog;
import common.utils.MarketUtils;
import common.utils.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 处理游戏进程相关逻辑
 *
 * @author F1ower
 * @since 2026-1-31
 */
@Service
@Slf4j
public class PlayProgressService {

    /**
     * 基础灵材限制
     */
    @Value("${spring.spiritual.base-limit}")
    private long baseLimit;

    @Value("${spring.spiritual.base-ratio}")
    private double baseRatio;

    /**
     * 计算灵市波动比例、灵材总数限制
     *
     * @param openDays 开店天数
     */
    @ExceptionLog("计算每日数据失败")
    public void computeDailyRatio(int openDays) {
        // 灵材市场今日比例 [0.7, 1.7]
        double waveRatio = MarketUtils.fetchWavePriceRatio();
        // 灵材总数比例
        double ratio = Math.max(1.0 + (openDays / 10.0) * baseRatio, 2.5);
        // 计算灵材总数
        long materialCount = Math.round(baseLimit * ratio);
        // 放入 threadLocal
        ThreadLocalUtils.add("waveRatio", waveRatio);
    }
}
