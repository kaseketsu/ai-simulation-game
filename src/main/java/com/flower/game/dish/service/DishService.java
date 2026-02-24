package com.flower.game.dish.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flower.game.ai.manager.QwenManager;
import com.flower.game.ai.models.entity.NameCreateResponse;
import com.flower.game.ai.templates.MealCreateTextTemplate;
import com.flower.game.base.models.entity.SpiritualRecipeBase;
import com.flower.game.base.service.ISpiritualRecipeBaseService;
import com.flower.game.dish.models.dto.NewMealGenerateRequest;
import com.flower.game.dish.models.dto.SeasoningAddRequest;
import com.flower.game.dish.models.dto.SeasoningBatchAddRequest;
import com.flower.game.dish.models.entity.SpiritualDishRepo;
import com.flower.game.dish.models.entity.SpiritualSeasoningBase;
import com.flower.game.dish.models.vo.MaterialVO;
import com.flower.game.dish.models.vo.NewMaelInfoVO;
import com.flower.game.market.models.entity.SpiritualMaterialsRepo;
import com.flower.game.market.service.ISpiritualMaterialsRepoService;
import com.flower.game.progress.model.dto.SpiritualRepoQueryRequest;
import com.flower.game.progress.model.entity.PlayProgress;
import com.flower.game.progress.model.vo.SpiritualRepoInfoVO;
import com.flower.game.progress.service.GamePlayProgressService;
import com.flower.game.progress.service.IPlayProgressService;
import common.annotations.ExceptionLog;
import common.config.AppConfig;
import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import common.manager.CosManager;
import common.page.PageVO;
import common.utils.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 灵膳相关服务
 */
@Service
@Slf4j
public class DishService {

    @Resource
    private ISpiritualSeasoningBaseService iSpiritualSeasoningBaseService;

    @Resource
    private MealCreateTextTemplate mealCreateTextTemplate;

    @Resource
    private QwenManager qwenManager;

    @Value("${spring.cos.save-path}")
    private String savePath;

    @Resource
    private CosManager cosManager;

    @Resource
    private AppConfig appConfig;

    @Resource
    private GamePlayProgressService gamePlayProgressService;

    @Resource
    private IPlayProgressService iPlayProgressService;

    @Resource
    private ISpiritualDishRepoService iSpiritualDishRepoService;

    @Resource
    private ISpiritualRecipeBaseService iSpiritualRecipeBaseService;

    @Resource
    private ISpiritualMaterialsRepoService iSpiritualMaterialsRepoService;

    // 修正语法错误 + 补全所有1-10阶倍率 + 规范命名
    private final Map<Integer, Double> storeMultiplier = new HashMap<>() {
        {
            // 初始化块：包裹所有put语句，语法合法
            put(1, 2.0);   // 1阶 - 200%
            put(2, 2.5);   // 2阶 - 250%
            put(3, 3.0);   // 3阶 - 300%
            put(4, 3.5);   // 4阶 - 350%
            put(5, 4.0);   // 5阶 - 400%
            put(6, 5.0);   // 6阶 - 500%
            put(7, 6.0);   // 7阶 - 600%
            put(8, 7.0);   // 8阶 - 700%
            put(9, 8.0);   // 9阶 - 800%
            put(10, 10.0); // 10阶 - 1000%
        }
    };

    private final Map<Integer, Double> materialMultiplier = new HashMap<>() {
        {
            put(1, 1.0);
            put(2, 2.0);
            put(3, 4.0);
            put(4, 10.0);
        }
    };

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
        // 获得主食材和辅助食材信息
        SpiritualMaterialsRepo mainInfo = iSpiritualMaterialsRepoService.getById(generateRequest.getMainId());
        SpiritualMaterialsRepo sideInfo = iSpiritualMaterialsRepoService.getById(generateRequest.getSideId());
        ThrowUtils.throwIf(mainInfo.getCount() <= 0 || sideInfo.getCount() <= 0, ErrorCode.NOT_FOUND_ERROR, "灵材数量不足");
        mainInfo.setCount(mainInfo.getCount() - 1);
        sideInfo.setCount(sideInfo.getCount() - 1);
        // 更新数据库
        iSpiritualMaterialsRepoService.updateById(mainInfo);
        iSpiritualMaterialsRepoService.updateById(sideInfo);
        // 查看配方表，看看是否有存在的灵膳
        LambdaQueryWrapper<SpiritualRecipeBase> recipeWrapper = new LambdaQueryWrapper<>();
        recipeWrapper.eq(SpiritualRecipeBase::getMainIngredient, generateRequest.getMainIngredient())
                .eq(SpiritualRecipeBase::getSideIngredient, generateRequest.getSideIngredient())
                .eq(SpiritualRecipeBase::getSeasoning, generateRequest.getSeasoning());
        // 灵膳配方信息
        SpiritualRecipeBase recipeBase = iSpiritualRecipeBaseService.getOne(recipeWrapper);
        boolean hasRecipe = recipeBase != null;
        // 最终填充信息
        SpiritualDishRepo res;
        if (hasRecipe) {
            String response = recipeBase.getResponse();
            log.info("灵膳配方存在，ai 生成结果为: {}", response);
            SpiritualDishRepo dishRepo = JSONUtil.toBean(response, SpiritualDishRepo.class);
            res = dishRepo;
            // 清除部分自动生成数据
            ObjUtils.removeAutoParams(dishRepo);
            // 添加当前 userId
            dishRepo.setUserId(generateRequest.getUserId());
            // 判断库中是否存在灵膳
            LambdaQueryWrapper<SpiritualDishRepo> dishWrapper = new LambdaQueryWrapper<>();
            dishWrapper.eq(SpiritualDishRepo::getUserId, generateRequest.getUserId())
                    .eq(SpiritualDishRepo::getName, dishRepo.getName())
                    .eq(SpiritualDishRepo::getIsDeleted, 0);
            SpiritualDishRepo dish = iSpiritualDishRepoService.getOne(dishWrapper);
            boolean hasDish = dish != null;
            if (hasDish) {
                // 当前 dishCount + 1
                dish.setCount(dish.getCount() + 1);
                iSpiritualDishRepoService.updateById(dish);
            } else {
                dishRepo.setCount(1);
                iSpiritualDishRepoService.save(dishRepo);
            }
        } else {
            // 生成新的名称
            log.info("请求 ai 获取新灵膳名称...");
            NameCreateResponse mealName = mealCreateTextTemplate.fetchTextResponse(generateRequest);
            // 这里先生成一张
            String imageUrl = generatePic(generateRequest);
            // 存入数据库
            SpiritualDishRepo spiritualDishRepo = new SpiritualDishRepo();
            BeanUtil.copyProperties(mealName, spiritualDishRepo);
            // 计算价格
            Long price = calculatePrice(generateRequest);
            spiritualDishRepo.setPrice(price);
            spiritualDishRepo.setUrl(imageUrl);
            spiritualDishRepo.setCount(1);
            spiritualDishRepo.setUserId(generateRequest.getUserId());
            // 存入数据库
            iSpiritualDishRepoService.save(spiritualDishRepo);
            // 存入配方表, mainName + sideName + seasoning = obj
            SpiritualRecipeBase spiritualRecipeBase = new SpiritualRecipeBase();
            spiritualRecipeBase.setMainIngredient(generateRequest.getMainIngredient());
            spiritualRecipeBase.setSideIngredient(generateRequest.getSideIngredient());
            spiritualRecipeBase.setSeasoning(generateRequest.getSeasoning());
            spiritualRecipeBase.setResponse(JSONUtil.toJsonStr(spiritualDishRepo));
            iSpiritualRecipeBaseService.save(spiritualRecipeBase);
            res = spiritualDishRepo;
        }
        // 构建返回对象
        NewMaelInfoVO newMaelInfoVO = new NewMaelInfoVO();
        newMaelInfoVO.setName(res.getName());
        newMaelInfoVO.setDescription(res.getDescription());
        newMaelInfoVO.setPrice(res.getPrice());
        newMaelInfoVO.setUrl(res.getUrl());
        log.info("返回对象构建完成，参数为: {}", JSONUtil.toJsonPrettyStr(newMaelInfoVO));
        return newMaelInfoVO;
    }

    /**
     * 请求获取食材原料
     *
     * @param request 分页请求
     * @return 分页食材
     */
    @ExceptionLog("获取食材原料失败")
    public PageVO<MaterialVO> fetchMaterials(SpiritualRepoQueryRequest request) {
        PageVO<SpiritualRepoInfoVO> infoVOPageVO = gamePlayProgressService.listSpiritualRepoByPage(request);
        List<SpiritualRepoInfoVO> records = infoVOPageVO.getRecords();
        // 转换为 vo（原逻辑：过滤掉稀有度不为 1 的）
        List<MaterialVO> materialVOS = records.stream().filter(r -> r.getCount() > 0).map(r -> {
            MaterialVO materialVO = new MaterialVO();
            BeanUtil.copyProperties(r, materialVO);
            return materialVO;
        }).toList();
        // 包装为 page
        return PageUtils.buildPageVO(materialVOS, request.getPageSize(), request.getCurrentPage());
    }

    /**
     * 计算灵膳价格
     *
     * @param generateRequest 生成请求
     * @return 生成的灵膳价格
     */
    private Long calculatePrice(NewMealGenerateRequest generateRequest) {
        // 获取当前用户的食肆品阶
        Long userId = generateRequest.getUserId();
        LambdaQueryWrapper<PlayProgress> progressWrapper = new LambdaQueryWrapper<>();
        progressWrapper.eq(PlayProgress::getUserId, userId)
                .eq(PlayProgress::getIsDeleted, 0);
        PlayProgress playProgress = iPlayProgressService.getOne(progressWrapper);
        Integer storeLevel = playProgress.getStoreLevel();
        // 获取主和副食材的加成倍率
        Integer mainIngredientRarity = generateRequest.getMainIngredientRarity();
        Integer sideIngredientRarity = generateRequest.getSideIngredientRarity();
        Double mainMulti = materialMultiplier.get(mainIngredientRarity);
        Double sideMulti = materialMultiplier.get(sideIngredientRarity);
        // 加成倍率
        Double multi = storeMultiplier.get(storeLevel);
        // 获取两个食材价格
        Long mainIngredientPrice = generateRequest.getMainIngredientPrice();
        Long sideIngredientPrice = generateRequest.getSideIngredientPrice();
        // 计算价格
        double price = 0.7 * mainIngredientPrice * mainMulti + 0.3 * sideIngredientPrice * sideMulti;
        price = price * multi;
        return Math.round(price);
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
