package com.flower.game.market.service;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flower.game.base.models.entity.SpiritualMaterialAllCat;
import com.flower.game.base.models.entity.SpiritualMaterialsBase;
import com.flower.game.base.service.ISpiritualMaterialsBaseService;
import com.flower.game.entrance.models.entity.SpiritualMaterialForRedis;
import com.flower.game.entrance.service.SpiritualMaterialLocalService;
import com.flower.game.market.models.dto.BuyMaterialRequest;
import com.flower.game.market.models.dto.SpiritualMaterialBaseRequest;
import com.flower.game.market.models.entity.SpiritualMaterialsRepo;
import com.flower.game.progress.model.entity.PlayProgress;
import com.flower.game.progress.model.entity.UserProperties;
import com.flower.game.progress.service.IPlayProgressService;
import com.flower.game.progress.service.IUserPropertiesService;
import common.annotations.ExceptionLog;
import common.constant.MarketConstant;
import common.exceptions.ErrorCode;
import common.manager.RedisManager;
import common.page.PageVO;
import common.utils.MarketUtils;
import common.utils.PageUtils;
import common.utils.ThrowUtils;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.LambdaConversionException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 灵材采买相关服务
 *
 * @author F1ower
 * @since 2026-1-31
 */
@Service
@Slf4j
public class MarketService {

    @Value("${spring.spiritual.redis-key}")
    private String redisKey;

    @Resource
    private RedisManager redisManager;

    @Resource
    private SpiritualMaterialLocalService spiritualMaterialLocalService;

    @Resource
    private IUserPropertiesService iUserPropertiesService;

    @Resource
    private ISpiritualMaterialsBaseService iSpiritualMaterialsBaseService;

    @Resource
    private IPlayProgressService iPlayProgressService;

    @Resource
    private ISpiritualMaterialsRepoService iSpiritualMaterialsRepoService;

    /**
     * 根据前端传递 type 不同，展示不同 type 的所有，分页展示
     *
     * @param request 基础灵材查询请求
     */
    @ExceptionLog("获取基础灵材失败")
    public PageVO<SpiritualMaterialAllCat> fetchSpiritualMaterial(final SpiritualMaterialBaseRequest request) {
        // 校验参数
        int type = request.getType();
        ThrowUtils.throwIf(type < 0 || type > 6, ErrorCode.PARAM_ERROR, "灵材种类不存在");
        // 从 redis 拿对应种类的灵材
        String key = redisKey + type;
        boolean isExist = redisManager.hasKey(key);
        // 如果不存在则重新刷新
        if (!isExist) {
            // 初始化到 redis
            spiritualMaterialLocalService.init();
        }
        // 从 redis 取出对应的值
        String res = redisManager.getValue(key, String.class);
        // 转为对应实体
        SpiritualMaterialForRedis materialForRedis = JSONUtil.toBean(res, SpiritualMaterialForRedis.class);
        // 获取 List
        List<SpiritualMaterialAllCat> catList = materialForRedis.getCatList();
        // 获取当日波动
        Double ratio = redisManager.getValue(MarketConstant.WAVE_RATIO, Double.class);
        double waveRatios = ratio == null ? 1.0 : ratio;
        for (SpiritualMaterialAllCat c : catList) {
            // 重新计算价格
            Long price = Math.round(c.getNormalPrice() * 1.0 * (1.0 + waveRatios));
            c.setNormalPrice(price);
        }
        // 分页
        return PageUtils.buildPageVO(catList, request.getPageSize(), request.getCurrentPage());
    }

    /**
     * 购买灵材
     *
     * @param request 购买请求
     */
    @ExceptionLog("购买灵材失败")
    @Transactional(rollbackFor = Exception.class)
    public void buyMaterial(final BuyMaterialRequest request) {
        // 获取所有属性
        Long userId = request.getUserId();
        String name = request.getName();
        Integer count = request.getCount();
        Long price = request.getPrice();
        // 获取玩家属性
        LambdaQueryWrapper<UserProperties> userPropWrapper = new LambdaQueryWrapper<>();
        userPropWrapper.eq(UserProperties::getUserId, userId)
                .eq(UserProperties::getIsDeleted, 0);
        UserProperties userProperties = iUserPropertiesService.getOne(userPropWrapper);
        // 获取基础灵材
        LambdaQueryWrapper<SpiritualMaterialsBase> baseMaterialWrapper = new LambdaQueryWrapper<>();
        baseMaterialWrapper.eq(SpiritualMaterialsBase::getName, name)
                .eq(SpiritualMaterialsBase::getIsDeleted, 0);
        SpiritualMaterialsBase materialsBase = iSpiritualMaterialsBaseService.getOne(baseMaterialWrapper);
        // 根据 groupId，找出所有四种 rarity
        Long groupId = materialsBase.getGroupId();
        LambdaQueryWrapper<SpiritualMaterialsBase> baseGroupWrapper = new LambdaQueryWrapper<>();
        baseGroupWrapper.eq(SpiritualMaterialsBase::getGroupId, groupId)
                .eq(SpiritualMaterialsBase::getIsDeleted, 0);
        List<SpiritualMaterialsBase> spiritualMaterialsBases = iSpiritualMaterialsBaseService.list(baseGroupWrapper);
        // 根据 rarity 分组
        Map<Integer, SpiritualMaterialsBase> rarityMap = new HashMap<>();
        spiritualMaterialsBases.forEach(spiritualMaterialsBase ->
                rarityMap.put(spiritualMaterialsBase.getRarity(), spiritualMaterialsBase));
        // 获取打折折扣
        Integer sense = userProperties.getSense();
        Double deduct = MarketUtils.fetchPriceRatio(sense);
        Long deductPrice = Math.round(deduct * price);
        // 查看是否买得起
        Long totalPrice = count * deductPrice;
        // 获取还剩的钱数
        LambdaQueryWrapper<PlayProgress> progressWrapper = new LambdaQueryWrapper<>();
        progressWrapper.eq(PlayProgress::getUserId, userId)
                .eq(PlayProgress::getIsDeleted, 0);
        PlayProgress playProgress = iPlayProgressService.getOne(progressWrapper);
        ThrowUtils.throwIf(playProgress == null, ErrorCode.NOT_FOUND_ERROR, "未找到游玩记录");
        Long earnedMoney = playProgress.getEarnedMoney();
        ThrowUtils.throwIf(earnedMoney < totalPrice, ErrorCode.MONEY_NOT_ENOUGH_ERROR, "剩余灵石不足以购买");
        // 更新 playProgress
        playProgress.setEarnedMoney(earnedMoney - totalPrice);
        iPlayProgressService.updateById(playProgress);
        // 获取不同稀有度数量
        int[] counts = MarketUtils.fetchEachCategoryCount(sense, count);
        // 更新业务灵材库
        LambdaQueryWrapper<SpiritualMaterialsRepo> repoWrapper = new LambdaQueryWrapper<>();
        repoWrapper.eq(SpiritualMaterialsRepo::getUserId, userId)
                .eq(SpiritualMaterialsRepo::getIsDeleted, 0);
        List<SpiritualMaterialsRepo> repos = iSpiritualMaterialsRepoService.list(repoWrapper);
        // 建立 baseId -> repo mapping
        Map<Long, SpiritualMaterialsRepo> repoMap = new HashMap<>();
        repos.forEach(repo -> repoMap.put(repo.getBaseId(), repo));
        List<SpiritualMaterialsRepo> saveOrUpdateList = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            int c = counts[i - 1];
            if (c == 0) continue;
            SpiritualMaterialsBase base = rarityMap.get(i);
            SpiritualMaterialsRepo repo;
            if (repoMap.containsKey(base.getId())) {
                // 更新
                repo = repoMap.get(base.getId());
                repo.setCount(repo.getCount() + c);
            } else {
                // 保存
                repo = new SpiritualMaterialsRepo();
                repo.setBaseId(base.getId());
                repo.setCount(c);
                repo.setUserId(userId);
            }
            saveOrUpdateList.add(repo);
        }
        iSpiritualMaterialsRepoService.saveOrUpdateBatch(saveOrUpdateList);
    }
}
