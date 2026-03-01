package com.flower.game.ai.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flower.game.cultivator.models.dto.CultivationCreateRequest;
import com.flower.game.cultivator.models.entity.CultivationBase;
import com.flower.game.cultivator.service.CultivationService;
import com.flower.game.cultivator.service.ICultivationBaseService;
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

    @Resource
    private CultivationService cultivationService;

    @Resource
    private ICultivationBaseService iCultivationBaseService;

    /**
     * 初始化 prompt 到 redis
     */
    @PostConstruct
    public void init() {
        try (
                InputStream textPromptStream = this.getClass().getResourceAsStream("/prompts/createMeal-text.txt");
                InputStream imagePromptStream = this.getClass().getResourceAsStream("/prompts/createMeal-image.txt");
                InputStream systemPromptStream = this.getClass().getResourceAsStream("/prompts/systemPrompt.txt");
        ) {
            // 转为 bufferedStream
            ThrowUtils.throwIf(textPromptStream == null, ErrorCode.PARAM_ERROR, "文本 prompt 不存在！");
            ThrowUtils.throwIf(imagePromptStream == null, ErrorCode.PARAM_ERROR, "图片 prompt 不存在！");
            ThrowUtils.throwIf(systemPromptStream == null, ErrorCode.PARAM_ERROR, "系统 prompt 不存在！");
            InputStreamReader textStreamReader = new InputStreamReader(textPromptStream);
            InputStreamReader imageStreamReader = new InputStreamReader(imagePromptStream);
            InputStreamReader systemStreamReader = new InputStreamReader(systemPromptStream);
            BufferedReader textBufferedReader = new BufferedReader(textStreamReader);
            BufferedReader imageBufferedReader = new BufferedReader(imageStreamReader);
            BufferedReader systemBufferedReader = new BufferedReader(systemStreamReader);
            StringBuilder textSb = new StringBuilder();
            StringBuilder imageSb = new StringBuilder();
            StringBuilder systemSb = new StringBuilder();
            textBufferedReader.lines().forEach(l -> textSb.append(l).append("\n"));
            imageBufferedReader.lines().forEach(l -> imageSb.append(l).append("\n"));
            systemBufferedReader.lines().forEach(l -> systemSb.append(l).append("\n"));
            // 存入 redis
            String textKey = redisKey + PromptConstant.TEXT;
            redisManager.addValueWithOutExpiration(textKey, textSb.toString());
            log.info("文本 prompt 存入 redis 成功，redis-key: {}", textKey);
            String imageKey = redisKey + PromptConstant.IMAGE;
            redisManager.addValueWithOutExpiration(imageKey, imageSb.toString());
            log.info("图片 prompt 存入 redis 成功, redis-key: {}", imageKey);
            String systemKey = redisKey + PromptConstant.SYSTEM;
            redisManager.addValueWithOutExpiration(systemKey, systemSb.toString());
        } catch (Exception ex) {
            log.error("初始化 prompt 失败, 原因是: {}", ex.getMessage());
            throw new BusinessException(ErrorCode.INIT_ERROR, "初始化 prompt 失败");
        }
    }

    /**
     * 生成修士，暂定 100 个阈值
     */
    @PostConstruct
    public void generateCultivators() {
        final long upperBound = 100L;
        // 获取当前 cultivator 数量
        LambdaQueryWrapper<CultivationBase> baseWrapper = new LambdaQueryWrapper<>();
        baseWrapper.eq(CultivationBase::getIsDeleted, 0);
        long count = iCultivationBaseService.count(baseWrapper);
        if (count >= upperBound) {
            return;
        }
        long left = upperBound - count;
        long batch = (left / 10) + 1L;
        for (long i = 0; i < batch; i++) {
            // 创建修士
            CultivationCreateRequest cultivationCreateRequest = new CultivationCreateRequest();
            cultivationCreateRequest.setBatchSize(10);
            cultivationCreateRequest.setRegion(-1);
            cultivationCreateRequest.setStoreType(-1);
            cultivationService.createCultivationByBatch(cultivationCreateRequest);
        }
    }
}
