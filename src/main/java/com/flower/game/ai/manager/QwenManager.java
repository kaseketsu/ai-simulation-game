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
     * 传入用户消息并获取消息
     *
     * @param newMealGenerateRequest 灵膳创建相关消息
     * @return ai 响应结果
     */
    @ExceptionLog("灵膳创建失败")
    public NameCreateResponse createNewMealName(NewMealGenerateRequest newMealGenerateRequest) {
        // 校验参数
        ParamsCheckUtils.checkObj(newMealGenerateRequest);
        // 创建 message
        String systemPrompt = redisManager.getValue(RedisKeyConstants.SYSTEM_PROMPT, String.class);
        String textPrompt = redisManager.getValue(RedisKeyConstants.TEXT_PROMPT, String.class);
        Generation generation = new Generation();
        // 生成请求信息
        String userMessage = textPrompt.formatted(
                JSONUtil.toJsonPrettyStr(newMealGenerateRequest)
        );
        GenerationParam params = fetchGenerationParam(userMessage, systemPrompt, textPrompt);
        GenerationResult callRes;
        try {
            callRes = generation.call(params);
        } catch (NoApiKeyException | InputRequiredException e) {
            log.error("文本模型调用失败. 调用模型: {}, 调用参数: {}, 错误原因: {}", textModel, JSONUtil.toJsonPrettyStr(params), e.getMessage());
            throw new BusinessException(ErrorCode.TEXT_CALL_ERROR);
        }
        log.info("ai 返回内容为: {}", JSONUtil.toJsonPrettyStr(callRes));
        // 获取 content
        String content = callRes.getOutput().getChoices().get(0).getMessage().getContent();
        log.info("content 为: {}", content);
        // 转换为对应实体类
        return JSONUtil.toBean(content, NameCreateResponse.class);
    }

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


    /**
     * 获取 ai 生成参数
     *
     * @param userMessage  用户信息
     * @param systemPrompt 系统提示
     * @param textPrompt   文本提示
     * @return 生成参数
     */
    private GenerationParam fetchGenerationParam(String userMessage, String systemPrompt, String textPrompt) {
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(systemPrompt)
                .build();
        log.error("即将请求 ai, 用户输入为: {}", userMessage);
        Message userMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(userMessage)
                .build();
        return GenerationParam.builder()
                .apiKey(apiKey)
                .model(textModel)
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
    }
}
