package com.flower.game.cultivator.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flower.game.ai.templates.CultivationCreateTextTemplate;
import com.flower.game.ai.templates.CultivationImageCreateTemplate;
import com.flower.game.cultivator.dao.CultivationPersonalityMapper;
import com.flower.game.cultivator.models.dto.CultivationCreateRequest;
import com.flower.game.cultivator.models.dto.CultivatorImageCreateRequest;
import com.flower.game.cultivator.models.entity.*;
import common.annotations.ExceptionLog;
import common.config.AppConfig;
import common.constants.PromptConstant;
import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import common.manager.CosManager;
import common.manager.RedisManager;
import common.utils.ThrowUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Resource
    private CultivationCreateTextTemplate cultivationCreateTextTemplate;

    @Resource
    private CultivationImageCreateTemplate cultivationImageCreateTemplate;

    @Resource
    private ICultivationBaseService iCultivationBaseService;

    @Resource
    private ICultivationPersonalityService iCultivationPersonalityService;

    @Resource
    private CosManager cosManager;

    private final ExecutorService virtualPool = Executors.newVirtualThreadPerTaskExecutor();

    /**
     * 每分钟执行一次
     */
    @Async("virtualPool")
    @Scheduled(cron = "0 0/1 * * * ?")
    public void regenerateCultivationPic() {
        try {
            // 查找没有 url 的修士
            log.info("定时任务: 修士图片生成开始了....");
            long now = System.currentTimeMillis();
            LambdaQueryWrapper<CultivationBase> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CultivationBase::getIsDeleted, 0)
                    .isNull(CultivationBase::getUrl)
                    .last("limit 1");
            List<CultivationBase> noUrlCultivators = iCultivationBaseService.list(queryWrapper);
            // 循环生成图片，每次休眠 1000 * 60 ms，阿里疑似不让并发
            for (CultivationBase cultivationBase : noUrlCultivators) {
                CultivatorImageCreateRequest createRequest = new CultivatorImageCreateRequest();
                // 查找性格
                Long baseId = cultivationBase.getId();
                LambdaQueryWrapper<CultivationPersonality> personalityWrapper = new LambdaQueryWrapper<>();
                personalityWrapper.eq(CultivationPersonality::getCultivationId, baseId)
                        .eq(CultivationPersonality::getIsDeleted, 0);
                CultivationPersonality personality = iCultivationPersonalityService.getOne(personalityWrapper);
                BeanUtil.copyProperties(cultivationBase, createRequest);
                BeanUtil.copyProperties(personality, createRequest);
                log.info("修士名称为: {} , 地区为: {}, 性别为: {} 的图片即将生成....", createRequest.getName(), createRequest.getRegion(), createRequest.getGender());
                fillUrl(baseId, createRequest);
                log.info("图片生成完成, 耗时: {} ms, 即将休眠 60s....", System.currentTimeMillis() - now);
            }
        } catch (Exception ex) {
            log.error("修士图片重新生成定时任务执行失败, 原因是: {}", ex.getMessage());
            //....
        }
    }

    /**
     * 将部分全局信息存入 redis
     */
    @PostConstruct
    public void init() {
        try (
                InputStream sysStream = this.getClass().getResourceAsStream(appConfig.getSystemPeople());
                InputStream textStream = this.getClass().getResourceAsStream(appConfig.getCreatePeople());
                InputStream imageStream = this.getClass().getResourceAsStream(appConfig.getCreatePeoplePic());
        ) {
            // 检验参数
            ThrowUtils.throwIf(sysStream == null, ErrorCode.NOT_FOUND_ERROR, "请检查修士生成系统 prompt 路径是否存在");
            ThrowUtils.throwIf(textStream == null, ErrorCode.NOT_FOUND_ERROR, "请检查修士生成 prompt 路径是否存在");
            ThrowUtils.throwIf(imageStream == null, ErrorCode.NOT_FOUND_ERROR, "请检查修士图片生成 prompt 路径是否存在");
            // 转为字符流
            InputStreamReader sysReader = new InputStreamReader(sysStream);
            InputStreamReader textReader = new InputStreamReader(textStream);
            InputStreamReader imageReader = new InputStreamReader(imageStream);
            // 转为缓冲流
            BufferedReader sysBuffer = new BufferedReader(sysReader);
            BufferedReader textBuffer = new BufferedReader(textReader);
            BufferedReader imageBuffer = new BufferedReader(imageReader);
            // 读取并存入 redis
            StringBuilder sysSb = new StringBuilder();
            StringBuilder textSb = new StringBuilder();
            StringBuilder imageSb = new StringBuilder();
            sysBuffer.lines().forEach(l -> sysSb.append(l).append("\n"));
            textBuffer.lines().forEach(l -> textSb.append(l).append("\n"));
            imageBuffer.lines().forEach(l -> imageSb.append(l).append("\n"));
            // 存入 redis
            String sysKey = redisKey + PromptConstant.SYSTEM_CULTIVATION;
            String textKey = redisKey + PromptConstant.CULTIVATION;
            String imageKey = redisKey + PromptConstant.CULTIVATION_PIC;
            redisManager.addValueWithOutExpiration(sysKey, sysSb.toString());
            log.info("修士生成系统 prompt 存入 redis 成功, key 为: {}", sysKey);
            redisManager.addValueWithOutExpiration(textKey, textSb.toString());
            log.info("修士生成 prompt 存入 redis 成功, key 为: {}", textKey);
            redisManager.addValueWithOutExpiration(imageKey, imageSb.toString());
            log.info("修士图片生成 prompt 存入 redis 成功, key 为: {}", imageKey);
        } catch (Exception ex) {
            log.error("prompt 初始化失败, 原因是: {}", ex.getMessage());
            throw new BusinessException(ErrorCode.INIT_ERROR, "prompt 初始化失败");
        }
    }

    /**
     * 骗量生成修士
     */
    @ExceptionLog("修士批量创建失败")
    @Transactional(rollbackFor = Exception.class)
    public void createCultivationByBatch(CultivationCreateRequest createRequest) {
        // 调用 template 获取 文本回复
        Customers customers = cultivationCreateTextTemplate.fetchTextResponse(createRequest);
        ThrowUtils.throwIf(customers == null || CollUtil.isEmpty(customers.getCustomers()), ErrorCode.TEXT_CALL_ERROR, "修士创建文本模型调用失败");
        List<Customer> customerList = customers.getCustomers();
        // 获取 ai 返回批次大小
        int actualBatchSize = customerList.size();
        // 批量存表
        for (Customer customer : customerList) {
            Identity identity = customer.getIdentity();
            Personality personality = customer.getPersonality();
            Dialogue dialogue = customer.getDialogue();
            CultivationBase cultivationBase = new CultivationBase();
            // 复制参数
            BeanUtil.copyProperties(identity, cultivationBase, CopyOptions.create().ignoreNullValue());
            BeanUtil.copyProperties(dialogue, cultivationBase, CopyOptions.create().ignoreNullValue());
            // 存入 base 库
            iCultivationBaseService.save(cultivationBase);
            // 获取 id
            Long baseId = cultivationBase.getId();
            // 异步生成图片
            CultivatorImageCreateRequest cultivatorImageCreateRequest = new CultivatorImageCreateRequest();
            BeanUtil.copyProperties(cultivationBase, cultivatorImageCreateRequest, CopyOptions.create().ignoreNullValue());
            BeanUtil.copyProperties(personality, cultivatorImageCreateRequest, CopyOptions.create().ignoreNullValue());
            fillUrl(baseId, cultivatorImageCreateRequest);
            // 存入 person 库
            CultivationPersonality cultivationPersonality = new CultivationPersonality();
            BeanUtil.copyProperties(personality, cultivationPersonality, CopyOptions.create().ignoreNullValue());
            cultivationPersonality.setTraits(JSONUtil.toJsonStr(personality.getTraits()));
            cultivationPersonality.setCultivationId(baseId);
            iCultivationPersonalityService.save(cultivationPersonality);
        }
        log.info("批量创建修士完成，成功处理 {} 条", actualBatchSize);
    }

    /**
     * 填充修士图片 url
     */
    public void fillUrl(Long id, CultivatorImageCreateRequest createRequest) {
        // 参数校验
        ThrowUtils.throwIf(createRequest == null, ErrorCode.PARAM_ERROR);
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAM_ERROR);
        long start = System.currentTimeMillis();
        log.info("图片生成任务id: {}, 开始.....", id);
        virtualPool.execute(() -> {
            try {
                String url = cultivationImageCreateTemplate.fetchPicUrl(createRequest);
                ThrowUtils.throwIf(url == null || url.isEmpty(), ErrorCode.IMAGE_CALL_ERROR);
                log.info("修士图片生成模型调用成功，生成 url 为: {}, 花费时间为: {}", url, System.currentTimeMillis() - start);
                // 写入 cos
                String cosUrl = cosManager.uploadToCOS(url);
                // 写入对应 id 的 cultivation_base
                CultivationBase cultivationBase = iCultivationBaseService.getById(id);
                cultivationBase.setUrl(cosUrl);
                iCultivationBaseService.updateById(cultivationBase);
                log.info("图片生成任务 id: {}, 已完成", id);
            } catch (Exception e) {
                log.error("任务 id: {}, 修士图片生成模型调用失败，原因为: {}", id, e.getMessage());
                throw new BusinessException(ErrorCode.IMAGE_CALL_ERROR, "修士生成失败");
            }
        });
    }
}
