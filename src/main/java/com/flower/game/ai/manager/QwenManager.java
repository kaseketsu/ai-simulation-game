package com.flower.game.ai.manager;

import common.manager.RedisManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 千问相关模型调用
 *
 * @author F1ower
 * @since 2026-2-14
 */
@Component
@Slf4j
public class QwenManager {

    @Resource
    private RedisManager redisManager;

    @Value("${spring.ali.text-model}")
    private String textModel;

    @Value("${spring.ali.image-model}")
    private String imageModel;

    @Value("${spring.ali.api-key}")
    private String apiKey;
}
