package com.flower.game.ai.manager;

import cn.hutool.json.JSONUtil;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.ParamUtils;
import com.flower.game.ai.models.entity.MealCreateMessage;
import com.openai.client.OpenAIClient;
import common.annotations.ExceptionLog;
import common.constants.RedisKeyConstants;
import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import common.manager.RedisManager;
import common.utils.ParamsCheckUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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

    @Value("${spring.ali.api-key}")
    private String apiKey;

    /**
     * 传入用户消息并获取消息
     *
     * @param mealCreateMessage 灵膳创建相关消息
     * @return ai 响应结果
     */
    @ExceptionLog("灵膳创建失败")
    public String createNewMealName(MealCreateMessage mealCreateMessage) {
        // 校验参数
        ParamsCheckUtils.checkObj(mealCreateMessage);
        // 创建 message
        String systemPrompt = redisManager.getValue(RedisKeyConstants.SYSTEM_PROMPT, String.class);
        String textPrompt = redisManager.getValue(RedisKeyConstants.TEXT_PROMPT, String.class);
        Generation generation = new Generation();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(systemPrompt)
                .build();
        String userMessage = textPrompt.formatted(mealCreateMessage.getMainIngredient(), mealCreateMessage.getSideIngredient(), mealCreateMessage.getSeasoning());
        log.error("即将请求 ai, 用户输入为: {}", userMessage);
        Message userMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(userMessage)
                .build();
        GenerationParam params = GenerationParam.builder()
                .apiKey(apiKey)
                .model(textModel)
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        GenerationResult callRes;
        try {
             callRes = generation.call(params);
        } catch (NoApiKeyException | InputRequiredException e) {
            log.error("文本模型调用失败. 调用模型: {}, 调用参数: {}, 错误原因: {}", textModel, JSONUtil.toJsonPrettyStr(params), e.getMessage());
            throw new BusinessException(ErrorCode.TEXT_CALL_ERROR);
        }
        return callRes.getMessage();
    }
}
