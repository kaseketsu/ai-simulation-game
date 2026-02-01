package com.flower.game.entrance.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flower.game.base.models.entity.SpiritualMaterialAllCat;
import com.flower.game.base.models.entity.SpiritualMaterialsBase;
import com.flower.game.base.service.ISpiritualMaterialsBaseService;
import com.flower.game.entrance.models.entity.SpiritualMaterialForRedis;
import common.annotations.ExceptionLog;
import common.constant.MarketConstant;
import common.manager.RedisManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏启动时将所有灵材放入 redis
 *
 * @author F1ower
 * @since 2026-1-31
 */
@Service
@Slf4j
public class SpiritualMaterialLocalService {

    @Resource
    private RedisManager redisManager;

    @Resource
    private ISpiritualMaterialsBaseService spiritualMaterialsBaseService;

    @Value("${spring.spiritual.redis-key}")
    private String redisKey;

    /**
     * 初始化灵材
     */
    @PostConstruct
    @ExceptionLog("灵材初始化失败")
    public void init() {
        log.info("灵材初始化开始....");
        LambdaQueryWrapper<SpiritualMaterialsBase> baseWrapper = new LambdaQueryWrapper<>();
        baseWrapper.eq(SpiritualMaterialsBase::getIsDeleted, 0);
        List<SpiritualMaterialsBase> spiritualMaterialsBases = spiritualMaterialsBaseService.list(baseWrapper);
        // 如果为空，返回
        if (CollUtil.isEmpty(spiritualMaterialsBases)) {
            log.warn("灵材基础库数量为空，请查看数据库情况!");
            return;
        }
        // 按照 groupId 进行分类
        log.info("开始按灵材 groupId 进行分类...");
        Map<Long, SpiritualMaterialAllCat> catByGroupId = new HashMap<>();
        spiritualMaterialsBases.forEach(s -> {
            Long groupId = s.getGroupId();
            if (!catByGroupId.containsKey(groupId)) {
                SpiritualMaterialAllCat cat = new SpiritualMaterialAllCat();
                cat.setType(s.getType());
                catByGroupId.put(groupId, cat);
            }
            Integer rarity = s.getRarity();
            SpiritualMaterialAllCat spiritualMaterialAllCat = catByGroupId.get(groupId);
            switch (rarity) {
                case 1 -> {
                    spiritualMaterialAllCat.setNormalName(s.getName());
                    spiritualMaterialAllCat.setNormalUrl(s.getUrl());
                    spiritualMaterialAllCat.setNormalPrice(s.getPrice());
                }
                case 2 -> {
                    spiritualMaterialAllCat.setRareName(s.getName());
                    spiritualMaterialAllCat.setRareUrl(s.getUrl());
                    spiritualMaterialAllCat.setRarePrice(s.getPrice());
                }
                case 3 -> {
                    spiritualMaterialAllCat.setSuperRareName(s.getName());
                    spiritualMaterialAllCat.setSuperRareUrl(s.getUrl());
                    spiritualMaterialAllCat.setSuperRarePrice(s.getPrice());
                }
                case 4 -> {
                    spiritualMaterialAllCat.setMythicalName(s.getName());
                    spiritualMaterialAllCat.setMythicalUrl(s.getUrl());
                    spiritualMaterialAllCat.setMythicalPrice(s.getPrice());
                }
            }
        });
        log.info("灵材按 groupId 分类成功!");
        // 按照 type 进行分类
        log.info("灵材开始按 type 分类....");
        List<SpiritualMaterialAllCat> all = new ArrayList<>();
        Map<Integer, List<SpiritualMaterialAllCat>> catListByType = new HashMap<>();
        for (SpiritualMaterialAllCat cat: catByGroupId.values()) {
            all.add(cat);
            int type = cat.getType();
            catListByType.computeIfAbsent(type, k -> new ArrayList<>()).add(cat);
        }
        log.info("灵材按 type 分类成功!");
        // 逐一序列华为 json，存入 redis
        log.info("灵材存入 redis 中...");
        for (Map.Entry<Integer, List<SpiritualMaterialAllCat>> entry: catListByType.entrySet()) {
            Integer type = entry.getKey();
            String catKey = redisKey + type;
            List<SpiritualMaterialAllCat> cats = entry.getValue();
            SpiritualMaterialForRedis spiritualMaterialForRedis = new SpiritualMaterialForRedis();
            spiritualMaterialForRedis.setCatList(cats);
            String js = JSONUtil.toJsonStr(spiritualMaterialForRedis);
            redisManager.addValueWithOutExpiration(catKey, js);
        }
        // 所有灵材
        SpiritualMaterialForRedis spiritualMaterialForRedis = new SpiritualMaterialForRedis();
        spiritualMaterialForRedis.setCatList(all);
        String js = JSONUtil.toJsonStr(spiritualMaterialForRedis);
        String catKey = redisKey + MarketConstant.ALL_TYPE;
        redisManager.addValue(catKey, js);
        log.info("灵材存入 redis 成功!");
        log.info("灵材初始化成功!");
    }
}
