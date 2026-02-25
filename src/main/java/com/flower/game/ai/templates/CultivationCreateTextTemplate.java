package com.flower.game.ai.templates;

import com.flower.game.ai.templates.abstractTemplates.AbstractTextModelCallTemplate;
import com.flower.game.cultivator.models.dto.CultivationCreateRequest;
import com.flower.game.cultivator.models.entity.Customers;
import common.constants.PromptConstant;
import common.manager.RedisManager;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 修士创建模板类
 *
 * @author F1ower
 * @since 2026-2-24
 */
@Service
public class CultivationCreateTextTemplate extends AbstractTextModelCallTemplate<CultivationCreateRequest, Customers> {

    @Resource
    private RedisManager redisManager;

    @Value("${spring.spiritual.redis-key}")
    private String redisKey;

    /**
     * 获取返回值类型
     *
     * @return 返回值类型
     */
    @Override
    public Class<Customers> fetchRespClass() {
        return Customers.class;
    }

    /**
     * 获取系统 prompt
     *
     * @return 系统 prompt
     */
    @Override
    public String fetchSysPrompt() {
        return redisManager.getValue(redisKey + PromptConstant.SYSTEM_CULTIVATION, String.class);
    }

    /**
     * 获取文本 prompt
     *
     * @return 文本 prompt
     */
    @Override
    public String fetchTextPrompt() {
        return redisManager.getValue(redisKey + PromptConstant.CULTIVATION, String.class);
    }
}
