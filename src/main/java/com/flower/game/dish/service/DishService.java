package com.flower.game.dish.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.flower.game.ai.manager.QwenManager;
import com.flower.game.ai.models.entity.NameCreateResponse;
import com.flower.game.base.models.entity.SpiritualMaterialAllCat;
import com.flower.game.base.models.entity.SpiritualMaterialsBase;
import com.flower.game.base.service.ISpiritualMaterialsBaseService;
import com.flower.game.dish.models.dto.NewMealGenerateRequest;
import com.flower.game.dish.models.dto.SeasoningAddRequest;
import com.flower.game.dish.models.dto.SeasoningBatchAddRequest;
import com.flower.game.dish.models.entity.SpiritualSeasoningBase;
import com.flower.game.dish.models.vo.NewMaelInfoVO;
import com.flower.game.entrance.models.entity.SpiritualMaterialForRedis;
import common.annotations.ExceptionLog;
import common.config.AppConfig;
import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import common.manager.CosManager;
import common.manager.RedisManager;
import common.utils.FileUtils;
import common.utils.ParamsCheckUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 灵膳相关服务
 */
@Service
@Slf4j
public class DishService {

    @Resource
    private ISpiritualSeasoningBaseService iSpiritualSeasoningBaseService;

    @Resource
    private QwenManager qwenManager;

    @Resource
    private ISpiritualMaterialsBaseService iSpiritualMaterialsBaseService;

    @Value("${spring.cos.save-path}")
    private String savePath;

    @Resource
    private CosManager cosManager;

    @Resource
    private AppConfig appConfig;

    @Value("${spring.spiritual.redis-key}")
    private String redisKey;

    @Resource
    private RedisManager redisManager;

    /**
     * 批量添加灵膳
     *
     * @param request 添加请求
     */
    @ExceptionLog("批量添加灵膳失败")
    public void addSeasoningByBatch(SeasoningBatchAddRequest request) {
        // 获取请求
        List<SeasoningAddRequest> requestList = request.getRequestList();
        // 判断参数
        if (CollUtil.isEmpty(requestList)) {
            return;
        }
        // 批量转化
        List<SpiritualSeasoningBase> bases = requestList.stream().map(r -> {
            SpiritualSeasoningBase spiritualSeasoningBase = new SpiritualSeasoningBase();
            BeanUtil.copyProperties(r, spiritualSeasoningBase);
            return spiritualSeasoningBase;
        }).toList();
        // 存入苦衷
        iSpiritualSeasoningBaseService.saveBatch(bases);
    }

    /**
     * 创建新的零膳
     *
     * @param generateRequest 灵膳生成请求
     * @return 新的灵膳
     */
    @ExceptionLog("新灵膳生成失败")
    @Transactional(rollbackFor = Exception.class)
    public NewMaelInfoVO createNewMeal(NewMealGenerateRequest generateRequest) {
        // 校验参数
        ParamsCheckUtils.checkObj(generateRequest);
        // 生成新的名称
        log.info("请求 ai 获取新灵膳名称...");
        NameCreateResponse mealName = qwenManager.createNewMealName(generateRequest);
        // 存入数据库
        String uuid = UUID.randomUUID().toString();
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String groupId = String.format("%s/%s", uuid, now);
        // 异步更新数据库
        addNewMealToDataBaseSync(generateRequest, mealName, groupId);
        // 这里先生成一张
        String imageUrl = generatePic(generateRequest);
        // 构建返回对象
        NewMaelInfoVO newMaelInfoVO = new NewMaelInfoVO();
        newMaelInfoVO.setName(mealName.getNormalName());
        newMaelInfoVO.setDescription(mealName.getNormalDescription());
        newMaelInfoVO.setUrl(imageUrl);
        // 更新 redis
        updateRedis(mealName);
        log.info("返回对象构建完成，参数为: {}", JSONUtil.toJsonPrettyStr(newMaelInfoVO));
        return newMaelInfoVO;
    }

    /**
     * 将 ai 返回的四个信息分别生成图片并保存到数据库（异步）
     *
     * @param response 返回结果
     */
    private void addNewMealToDataBaseSync(NewMealGenerateRequest generateRequest, NameCreateResponse response, String groupId) {
        try (ExecutorService virtualPool = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 4; i++) {
                int finalI = i;
                virtualPool.submit(() -> {
                    try {
                        log.info("虚拟线程任务号: {}, 启动...", groupId);
                        long startTime = System.currentTimeMillis();
                        SpiritualMaterialsBase spiritualMaterialsBase = fetchMaterialBase(generateRequest, response, groupId, finalI);
                        iSpiritualMaterialsBaseService.save(spiritualMaterialsBase);
                        long costTime = System.currentTimeMillis() - startTime;
                        log.info("虚拟线程任务号: {}，结束, 耗时: {}ms", groupId, costTime);
                    } catch (Exception e) {
                        log.info("虚拟线程任务号: {}，执行失败，原因是: {}", groupId, e.getMessage());
                        throw new BusinessException(ErrorCode.VIRTUAL_THREAD_ERROR, "食材保存失败");
                    }
                });
            }
        }
    }

    /**
     * 更新 redis 数据
     *
     * @param response 响应
     */
    private void updateRedis(NameCreateResponse response) {
        // 包装为 allCat，更新 redis
        log.info("准备将新生成内容存入 redis 中....");
        SpiritualMaterialAllCat spiritualMaterialAllCat = new SpiritualMaterialAllCat();
        BeanUtil.copyProperties(response, spiritualMaterialAllCat);
        String key = redisKey + response.getType();
        String value = redisManager.getValue(key, String.class);
        SpiritualMaterialForRedis materialForRedis = JSONUtil.toBean(value, SpiritualMaterialForRedis.class);
        materialForRedis.getCatList().add(spiritualMaterialAllCat);
        redisManager.addValueWithOutExpiration(key, JSONUtil.toJsonStr(materialForRedis));
        log.info("redis 更新完毕，redisKey: {}", key);
    }

    /**
     * 根据稀有度生成不同食材
     *
     * @param generateRequest 生成请求
     * @param response        文本模型响应
     * @param groupId         租 id
     * @param i               稀有度
     * @return 填充好属性的食材
     */
    private SpiritualMaterialsBase fetchMaterialBase(NewMealGenerateRequest generateRequest, NameCreateResponse response, String groupId, int i) {
        SpiritualMaterialsBase spiritualMaterialsBase = new SpiritualMaterialsBase();
        BeanUtil.copyProperties(response, spiritualMaterialsBase);
        switch (i) {
            case 0 -> {
                spiritualMaterialsBase.setDescription(response.getNormalDescription());
                spiritualMaterialsBase.setName(response.getNormalName());
                spiritualMaterialsBase.setPrice(response.getNormalPrice());
            }
            case 1 -> {
                spiritualMaterialsBase.setDescription(response.getRareDescription());
                spiritualMaterialsBase.setName(response.getRareName());
                spiritualMaterialsBase.setPrice(response.getRarePrice());
            }
            case 2 -> {
                spiritualMaterialsBase.setDescription(response.getSuperRareDescription());
                spiritualMaterialsBase.setName(response.getSuperRareName());
                spiritualMaterialsBase.setPrice(response.getSuperRarePrice());
            }
            case 3 -> {
                spiritualMaterialsBase.setDescription(response.getMythicalDescription());
                spiritualMaterialsBase.setName(response.getMythicalName());
                spiritualMaterialsBase.setPrice(response.getMythicalPrice());
            }
        }
        String url = generatePic(generateRequest);
        spiritualMaterialsBase.setUrl(url);
        spiritualMaterialsBase.setGroupId(groupId);
        spiritualMaterialsBase.setRarity(i);
        return spiritualMaterialsBase;
    }

    private String generatePic(NewMealGenerateRequest generateRequest) {
        // 请求生成新的图片
        log.info("即将下载图片到本地....");
        String picUrl = qwenManager.createNewMealPic(generateRequest);
        // 拼接保存路径
        String uuid = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        String formatted = LocalDateTimeUtil.format(now, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        // 图片的完整保存路径
        String fullPath = "%s/%s_%s.png".formatted(savePath, uuid, formatted);
        // 生成临时文件
        Path path;
        try {
            path = Paths.get(fullPath);
            Path parentDir = path.getParent();
            // 补全父目录
            if (parentDir != null) {
                Files.createDirectories(parentDir);
                log.info("父目录创建完毕: {}", parentDir.toAbsolutePath());
            }
            // 创建文件
            Files.createFile(path);
            log.info("文件创建完毕, 路径为: {}", path.toAbsolutePath());
        } catch (IOException ex) {
            log.error("文件生成失败，原因是: {}", ex.getMessage());
            throw new BusinessException(ErrorCode.FILE_CREATE_ERROR);
        }
        try (FileOutputStream outputStream = new FileOutputStream(path.toFile())) {
            HttpUtil.download(picUrl, outputStream, true);
        } catch (Exception ex) {
            log.error("图片下载失败，url: {}, 保存路径: {}, 原因为: {}", picUrl, fullPath, ex.getMessage());
            throw new BusinessException(ErrorCode.DOWNLOAD_ERROR, "图片下载失败");
        }
        fullPath = path.toAbsolutePath().toString();
        log.info("图片下载成功, 保存路径为: {}", fullPath);
        // 保存到腾讯云存储
        String fileName = FileUtils.fetchFileName("png");
        String putPath = appConfig.getImageSavePath() + "/" + fileName;
        cosManager.putLocalFile(putPath, fullPath);
        log.info("上传到腾讯云存储成功，文件路径: {}", putPath);
        // 拼接 url
        String imageUrl = appConfig.getCosImagePrefix() + putPath;
        log.info("前端展示图片 url 为: {}", imageUrl);
        return imageUrl;
    }
}
