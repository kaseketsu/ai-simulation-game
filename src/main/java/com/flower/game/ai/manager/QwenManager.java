package com.flower.game.ai.manager;

import cn.hutool.json.JSONUtil;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import com.alibaba.dashscope.utils.ParamUtils;
import com.flower.game.ai.models.entity.MealCreateMessage;
import com.flower.game.ai.models.entity.NameCreateResponse;
import com.flower.game.dish.models.dto.NewMealGenerateRequest;
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
import java.util.HashMap;
import java.util.Map;

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
