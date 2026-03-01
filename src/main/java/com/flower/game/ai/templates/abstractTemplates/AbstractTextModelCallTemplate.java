package com.flower.game.ai.templates.abstractTemplates;

import cn.hutool.json.JSONUtil;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import common.manager.RedisManager;
import common.utils.ParamsCheckUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;

/**
 * 文本模型调用模板类
 *
 * @author F1ower
 * @since 2026-2-24
 */
@Slf4j
public abstract class AbstractTextModelCallTemplate<T, R> {


    @Value("${spring.ali.text-model}")
    private String textModel;

    @Value("${spring.ali.api-key}")
    private String apiKey;

    /**
     * 传入指定类型请求，返回指定类型响应
     *
     * @param request 请求
     * @return 响应
     */
    public R fetchTextResponse(T request) {
        // 校验参数
        ParamsCheckUtils.checkObj(request);
        // 创建 message
        String sysPrompt = fetchSysPrompt();
        String textPrompt = fetchTextPrompt();
        Generation generation = new Generation();
        // 生成请求信息
        String userMessage = textPrompt.formatted(
                JSONUtil.toJsonPrettyStr(request)
        );
        GenerationParam params = fetchGenerationParam(userMessage, sysPrompt);
        GenerationResult callRes;
        try {
            callRes = generation.call(params);
        } catch (NoApiKeyException | InputRequiredException e) {
            log.error("文本模型调用失败. 调用模型: {}, 调用参数: {}, 错误原因: {}", textModel, JSONUtil.toJsonPrettyStr(params), e.getMessage());
            throw new BusinessException(ErrorCode.TEXT_CALL_ERROR);
        }
        // 获取 content
        String content = callRes.getOutput().getChoices().get(0).getMessage().getContent();
        log.info("content 为: {}", content);
        // 转换为对应实体类
        return JSONUtil.toBean(content, fetchRespClass());
    }

    /**
     * 获取 ai 生成参数
     *
     * @param userMessage  用户信息
     * @param systemPrompt 系统提示
     * @return 生成参数
     */
    private GenerationParam fetchGenerationParam(String userMessage, String systemPrompt) {
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

    /**
     * 获取返回值类型
     *
     * @return 返回值类型
     */
    public abstract Class<R> fetchRespClass();

    /**
     * 获取系统 prompt
     *
     * @return 系统 prompt
     */
    public abstract String fetchSysPrompt();

    /**
     * 获取文本 prompt
     *
     * @return 文本 prompt
     */
    public abstract String fetchTextPrompt();
}
