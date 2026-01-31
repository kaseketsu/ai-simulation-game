package com.flower.game.entrance.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flower.game.base.models.entity.SpiritualMaterialsBase;
import com.flower.game.base.service.ISpiritualMaterialsBaseService;
import common.manager.RedisManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 游戏启动时将所有灵材放入 redis
 *
 * @author F1ower
 * @since 2026-1-31
 */
@Service
public class SpiritualMaterialLocalService {

    @Resource
    private RedisManager redisManager;

    @Resource
    private ISpiritualMaterialsBaseService spiritualMaterialsBaseService;

    /**
     * 初始化灵材
     */
    @PostConstruct
    public void init() {
        LambdaQueryWrapper<SpiritualMaterialsBase> baseWrapper = new LambdaQueryWrapper<>();
        baseWrapper.eq(SpiritualMaterialsBase::getIsDeleted, 0);
        List<SpiritualMaterialsBase> spiritualMaterialsBases = spiritualMaterialsBaseService.list(baseWrapper);

    }
}
