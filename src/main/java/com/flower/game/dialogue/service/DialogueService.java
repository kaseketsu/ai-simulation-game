package com.flower.game.dialogue.service;

import common.constants.PromptConstant;
import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import common.manager.RedisManager;
import common.utils.ThrowUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 对话服务类
 *
 * @author F1ower
 * @since 2026-2-25
 */
@Service
@Slf4j
public class DialogueService {

    @Resource
    private RedisManager redisManager;

    /**
     * 初始化 prompt 到 redis
     */
    @PostConstruct
    public void init() {
        try (
                InputStream textPromptStream = this.getClass().getResourceAsStream("/prompts/dialoguePrompt.txt");
        ) {
            String key = redisManager.addStream(textPromptStream, PromptConstant.DIALOGUE);
            log.info("文本 prompt 存入 redis 成功，redis-key: {}", key);
        } catch (Exception ex) {
            log.error("初始化 prompt 失败, 原因是: {}", ex.getMessage());
            throw new BusinessException(ErrorCode.INIT_ERROR, "初始化 prompt 失败");
        }
    }



}


















