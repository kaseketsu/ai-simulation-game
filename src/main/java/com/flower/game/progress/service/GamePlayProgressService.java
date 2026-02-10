package com.flower.game.progress.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flower.game.base.models.entity.SpiritualMaterialsBase;
import com.flower.game.base.service.ISpiritualMaterialsBaseService;
import com.flower.game.market.models.entity.SpiritualMaterialsRepo;
import com.flower.game.market.service.ISpiritualMaterialsRepoService;
import com.flower.game.progress.model.dto.DailyInfoComputeRequest;
import com.flower.game.progress.model.dto.SpiritualRepoQueryRequest;
import com.flower.game.progress.model.vo.SpiritualRepoInfoVO;
import common.annotations.ExceptionLog;
import common.constant.MarketConstant;
import common.exceptions.ErrorCode;
import common.manager.RedisManager;
import common.page.PageVO;
import common.utils.MarketUtils;
import common.utils.PageUtils;
import common.utils.ThreadLocalUtils;
import common.utils.ThrowUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理游戏进程相关逻辑
 *
 * @author F1ower
 * @since 2026-1-31
 */
@Service
@Slf4j
public class GamePlayProgressService {

    @Resource
    private RedisManager redisManager;

    @Resource
    private ISpiritualMaterialsRepoService iSpiritualMaterialsRepoService;

    @Resource
    private ISpiritualMaterialsBaseService iSpiritualMaterialsBaseService;

    /**
     * 计算灵市波动比例、灵材总数限制
     *
     * @param request 计算请求
     */
    @ExceptionLog("计算每日数据失败")
    public void computeDailyRatio(DailyInfoComputeRequest request) {
        // 灵材市场今日比例 [0.7, 1.7]
        double waveRatio = MarketUtils.fetchWavePriceRatio();
        // 判断是否有 waveRatio
        if (redisManager.hasKey(MarketConstant.WAVE_RATIO)) {
            redisManager.removeKey(MarketConstant.WAVE_RATIO);
        }
        // 放入 threadLocal
        redisManager.addValue(MarketConstant.WAVE_RATIO, waveRatio);
        log.info("今日零市波动比例为: {}", waveRatio);
    }

    /**
     * 查询灵材仓库
     *
     * @param queryRequest 查询请求
     * @return 灵材仓库信息
     */
    @ExceptionLog("查询用户灵材仓库信息失败")
    public PageVO<SpiritualRepoInfoVO> listSpiritualRepoByPage(final SpiritualRepoQueryRequest queryRequest) {
        // 校验参数
        ThrowUtils.throwIf(queryRequest.getUserId() == null, ErrorCode.PARAM_ERROR, "请求 id 不能为空");
        // 获取 repo
        LambdaQueryWrapper<SpiritualMaterialsRepo> repoWrapper = new LambdaQueryWrapper<>();
        repoWrapper.eq(SpiritualMaterialsRepo::getUserId, queryRequest.getUserId())
                .eq(SpiritualMaterialsRepo::getIsDeleted, 0);
        List<SpiritualMaterialsRepo> repos = iSpiritualMaterialsRepoService.list(repoWrapper);
        // 校验返回结果
        if (CollUtil.isEmpty(repos)) {
            return PageVO.emptyPage();
        }
        // 建立 baseId -> repo mapping
        Map<Long, SpiritualMaterialsRepo> repoMap = new HashMap<>();
        for (SpiritualMaterialsRepo repo : repos) {
            repoMap.put(repo.getBaseId(), repo);
        }
        // 获取 baseId 集合
        List<Long> idList = repos.stream().map(SpiritualMaterialsRepo::getBaseId).toList();
        // 批量获取 base
        List<SpiritualMaterialsBase> baseList = iSpiritualMaterialsBaseService.listByIds(idList);
        // 批量设置属性
        List<SpiritualRepoInfoVO> infoVOS = new ArrayList<>();
        baseList.forEach(base -> {
            Long baseId = base.getId();
            SpiritualMaterialsRepo spiritualMaterialsRepo = repoMap.get(baseId);
            SpiritualRepoInfoVO spiritualRepoInfoVO = new SpiritualRepoInfoVO();
            BeanUtil.copyProperties(base, spiritualRepoInfoVO);
            BeanUtil.copyProperties(spiritualMaterialsRepo, spiritualRepoInfoVO);
            spiritualRepoInfoVO.setId(spiritualMaterialsRepo.getId());
            infoVOS.add(spiritualRepoInfoVO);
        });
        // 分页返回
        return PageUtils.buildPageVO(infoVOS, queryRequest.getPageSize(), queryRequest.getCurrentPage());
    }
}
