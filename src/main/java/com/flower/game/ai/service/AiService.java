package com.flower.game.ai.service;

import common.constants.PromptConstant;
import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import common.manager.RedisManager;
import common.utils.ThreadLocalUtils;
import common.utils.ThrowUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * ai 相关服务类
 *
 * @author F1ower
 * @since 2026-2-14
 */
@Service
@Slf4j
public class AiService {

    @Value("${spring.spiritual.redis-key}")
    private String redisKey;

    @Resource
    private RedisManager redisManager;

    /**
     * 初始化 prompt 到 redis
     */
    @PostConstruct
    public void init() {
        try (
                InputStream textPromptStream = this.getClass().getResourceAsStream("/prompts/createMeal-text.txt");
                InputStream imagePromptStream = this.getClass().getResourceAsStream("/prompts/createMeal-image.txt")
        ) {
            // 转为 bufferedStream
            ThrowUtils.throwIf(textPromptStream == null, ErrorCode.PARAM_ERROR, "文本 prompt 不存在！");
            ThrowUtils.throwIf(imagePromptStream == null, ErrorCode.PARAM_ERROR, "图片 prompt 不存在！");
            InputStreamReader textStreamReader = new InputStreamReader(textPromptStream);
            InputStreamReader imageStreamReader = new InputStreamReader(imagePromptStream);
            BufferedReader textBufferedReader = new BufferedReader(textStreamReader);
            BufferedReader imageBufferedReader = new BufferedReader(imageStreamReader);
            StringBuilder textSb = new StringBuilder();
            StringBuilder imageSb = new StringBuilder();
            textBufferedReader.lines().forEach(l -> textSb.append(l).append("\n"));
            imageBufferedReader.lines().forEach(l -> imageSb.append(l).append("\n"));
            // 存入 redis
            String textKey = redisKey + PromptConstant.TEXT;
            redisManager.addValue(textKey, textSb.toString());
            log.info("文本 prompt 存入 redis 成功，redis-key: {}", textKey);
            String imageKey = redisKey + PromptConstant.IMAGE;
            redisManager.addValue(imageKey, imageSb.toString());
            log.info("图片 prompt 存入 redis 成功, redis-key: {}", imageKey);
        } catch (Exception ex) {
            log.error("初始化 prompt 失败, 原因是: {}", ex.getMessage());
            throw new BusinessException(ErrorCode.INIT_ERROR, "初始化 prompt 失败");
        }
    }
}
