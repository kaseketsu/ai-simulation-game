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

    /**
     * 创建新灵膳图片
     *
     * @param newMealGenerateRequest 生成请求
     * @return 新灵膳图片 url
     */
    public String createNewMealPic(NewMealGenerateRequest newMealGenerateRequest) {
        // 校验参数
        ParamsCheckUtils.checkObj(newMealGenerateRequest);
        String imagePrompt = redisManager.getValue(RedisKeyConstants.IMAGE_PROMPT, String.class);
        String prompt = imagePrompt.formatted(JSONUtil.toJsonPrettyStr(newMealGenerateRequest));
        return callImageModel(prompt);
    }

    /**
     * 根据 prompt 调用模型并返回 url
     *
     * @param prompt 提示词
     * @return url
     */
    private String callImageModel(String prompt) {
        log.info("即将调用模型：{}, 请求提示词为: {}", imageModel, prompt);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("prompt_extend", true);
        parameters.put("watermark", false);
        ImageSynthesisParam param =
                ImageSynthesisParam.builder()
                        .apiKey(apiKey)
                        .model(imageModel)
                        .prompt(prompt)
                        .n(1)
                        .size("1328*1328")
                        .parameters(parameters)
                        .build();
        ImageSynthesis imageSynthesis = new ImageSynthesis();
        ImageSynthesisResult result;
        try {
            log.info("---同步调用模型: {}，请等待任务执行----", imageModel);
            result = imageSynthesis.call(param);
        } catch (ApiException | NoApiKeyException e) {
            log.error("图像模型调用失败. 调用模型: {}, 调用参数: {}, 错误原因: {}", textModel, JSONUtil.toJsonPrettyStr(param), e.getMessage());
            throw new BusinessException(ErrorCode.TEXT_CALL_ERROR);
        }
        // 返回 url
        return result.getOutput().getResults().get(0).get("url");
    }
}
