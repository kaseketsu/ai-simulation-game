package com.flower.game.ai.templates;

import com.flower.game.ai.models.entity.NameCreateResponse;
import com.flower.game.dish.models.dto.NewMealGenerateRequest;
import common.constants.RedisKeyConstants;
import common.manager.RedisManager;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 灵膳创建模板
 *
 * @author F1ower
 * @since 2026-2-24
 */
@Service
public class MealCreateTextTemplate extends AbstractTextModelCallTemplate<NewMealGenerateRequest, NameCreateResponse> {

    @Resource
    private RedisManager redisManager;

    /**
     * 获取返回值 class
     *
     * @return class 对象
     */
    @Override
    public Class<NameCreateResponse> fetchRespClass() {
        return NameCreateResponse.class;
    }

    /**
     * 获取 sysPrompt
     *
     * @return sysPrompt
     */
    @Override
    public String fetchSysPrompt() {
        return redisManager.getValue(RedisKeyConstants.SYSTEM_PROMPT, String.class);
    }

    /**
     * 获取 textPrompt
     *
     * @return textPrompt
     */
    @Override
    public String fetchTextPrompt() {
        return redisManager.getValue(RedisKeyConstants.TEXT_PROMPT, String.class);
    }
}
