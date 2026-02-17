package com.flower.game.dish.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import com.flower.game.ai.manager.QwenManager;
import com.flower.game.ai.models.entity.NameCreateResponse;
import com.flower.game.base.models.entity.SpiritualMaterialsBase;
import com.flower.game.base.service.ISpiritualMaterialsBaseService;
import com.flower.game.dish.models.dto.NewMealGenerateRequest;
import com.flower.game.dish.models.dto.SeasoningAddRequest;
import com.flower.game.dish.models.dto.SeasoningBatchAddRequest;
import com.flower.game.dish.models.entity.SpiritualSeasoningBase;
import com.flower.game.dish.models.vo.NewMaelInfoVO;
import common.annotations.ExceptionLog;
import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import common.utils.ParamsCheckUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.List;

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
        SpiritualMaterialsBase spiritualMaterialsBase = new SpiritualMaterialsBase();
        BeanUtil.copyProperties(mealName, spiritualMaterialsBase);
        iSpiritualMaterialsBaseService.save(spiritualMaterialsBase);
        // 请求生成新的图片
        log.info("即将下载图片到本地....");
        String picUrl = qwenManager.createNewMealPic(generateRequest);
        try (FileOutputStream outputStream = new FileOutputStream(savePath)) {
            HttpUtil.download(picUrl, outputStream, true);
        } catch (Exception ex) {
            log.error("图片下载失败，url: {}, 保存路径: {}, 原因为: {}", picUrl, savePath, ex.getMessage());
            throw new BusinessException(ErrorCode.DOWNLOAD_ERROR, "图片下载失败");
        }
        log.info("图片下载成功, 保存路径为: {}", savePath);
        // 保存到腾讯云存储
        return null;
    }
}
