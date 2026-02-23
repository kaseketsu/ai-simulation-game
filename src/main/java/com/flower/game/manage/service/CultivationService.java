package com.flower.game.manage.service;

import common.config.AppConfig;
import common.constants.PromptConstant;
import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import common.manager.RedisManager;
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
 * 修士生成、对话等相关服务类
 *
 * @author F1ower
 * @since 2026-2-22
 */
@Slf4j
@Service
public class CultivationService {

    @Resource
    private RedisManager redisManager;

    @Resource
    private AppConfig appConfig;

    @Value("${spring.spiritual.redis-key}")
    private String redisKey;

    /**
     * 将部分全局信息存入 redis
     */
    // todo: 加入新的 prompt，修士放入 redis，如果超过 50 个就随机选
    @PostConstruct
    public void init() {
        try (
                InputStream sysStream = this.getClass().getResourceAsStream(appConfig.getSystemPeople());
                InputStream textStream = this.getClass().getResourceAsStream(appConfig.getCreatePeople());
        ) {
            // 检验参数
            ThrowUtils.throwIf(sysStream == null, ErrorCode.NOT_FOUND_ERROR, "请检查修士生成系统 prompt 路径是否存在");
            ThrowUtils.throwIf(textStream == null, ErrorCode.NOT_FOUND_ERROR, "请检查修士生成 prompt 路径是否存在");
            // 转为字符流
            InputStreamReader sysReader = new InputStreamReader(sysStream);
            InputStreamReader textReader = new InputStreamReader(textStream);
            // 转为缓冲流
            BufferedReader sysBuffer = new BufferedReader(sysReader);
            BufferedReader textBuffer = new BufferedReader(textReader);
            // 读取并存入 redis
            StringBuilder sysSb = new StringBuilder();
            StringBuilder textSb = new StringBuilder();
            sysBuffer.lines().forEach(l -> sysSb.append(l).append("\n"));
            textBuffer.lines().forEach(l -> textSb.append(l).append("\n"));
            // 存入 redis
            String sysKey = redisKey + PromptConstant.SYSTEM_CULTIVATION;
            String textKey = redisKey + PromptConstant.CULTIVATION;
            redisManager.addValueWithOutExpiration(sysKey, sysSb.toString());
            log.info("修士生成系统 prompt 存入 redis 成功, key 为: {}", sysKey);
            redisManager.addValueWithOutExpiration(textKey, textSb.toString());
            log.info("修士生成 prompt 存入 redis 成功, key 为: {}", textKey);
        } catch (Exception ex) {
            log.error("prompt 初始化失败, 原因是: {}", ex.getMessage());
            throw new BusinessException(ErrorCode.INIT_ERROR, "prompt 初始化失败");
        }
    }

    // todo: 守护线程生成直到 redis 里有 50 个, 先落库再放入 redis
}
