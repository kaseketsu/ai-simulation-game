package com.flower.game.ai.manager;

import com.openai.client.OpenAIClient;
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
    private OpenAIClient openAIClient;

    @Value("${spring.ali.text-model}")
    private String textModel;

    /**
     * 传入用户消息并获取消息
     *
     * @param userMessage 用户消息
     * @return ai 响应结果
     */
    public String fetchResponse(String userMessage) {
        // todo
        return null;
    }
}
