package com.flower.game.ai.templates;

import com.flower.game.ai.templates.abstractTemplates.AbstractImageModelCallTemplate;
import com.flower.game.cultivator.models.dto.CultivatorImageCreateRequest;
import common.constants.PromptConstant;
import common.manager.RedisManager;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;

/**
 * 修士图片生成模板类
 *
 * @author F1ower
 * @since 2026-2-25
 */
public class CultivationImageCreateTemplate extends AbstractImageModelCallTemplate<CultivatorImageCreateRequest> {

    @Resource
    private RedisManager redisManager;

    @Value("${spring.spiritual.redis-key}")
    private String redisKey;

    /**
     * 获取修士图片生成 prompt
     *
     * @return 图片生成 prompt
     */
    @Override
    public String fetchImagePrompt() {
        return redisManager.getValue(redisKey + PromptConstant.CULTIVATION_PIC, String.class);
    }
}
