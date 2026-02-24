package com.flower.game.ai.templates;

import com.flower.game.ai.templates.abstractTemplates.AbstractImageModelCallTemplate;
import com.flower.game.dish.models.dto.NewMealGenerateRequest;
import common.constants.RedisKeyConstants;
import common.manager.RedisManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 灵膳图片生成模板类
 *
 * @author F1ower
 * @since 2026-2-24
 */
@Service
@Slf4j
public class MealCreateImageTemplate extends AbstractImageModelCallTemplate<NewMealGenerateRequest> {

    @Resource
    private RedisManager redisManager;

    /**
     * 获取图片生成提示词
     *
     * @return 图片生成提示词
     */
    @Override
    public String fetchImagePrompt() {
        return redisManager.getValue(RedisKeyConstants.IMAGE_PROMPT, String.class);
    }
}
