package com.flower.game.dish.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.flower.game.dish.models.dto.SeasoningBatchAddRequest;
import com.flower.game.dish.models.entity.SpiritualSeasoningBase;
import com.flower.game.dish.service.DishService;
import com.flower.game.entrance.models.entity.SeasoningsForRedis;
import common.baseEntities.BaseResponse;
import common.constant.MarketConstant;
import common.manager.RedisManager;
import common.page.PageRequest;
import common.page.PageVO;
import common.utils.PageUtils;
import common.utils.ParamsCheckUtils;
import common.utils.ResultUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 灵膳制作控制器
 *
 * @author F1ower
 * @since 2026-2-11
 */
@RestController
@RequestMapping("/dish")
public class DishMakeController {

    @Resource
    private DishService dishService;

    @Resource
    private RedisManager redisManager;

    @Value("${spring.spiritual.redis-key}")
    private String redisKey;


    /**
     * 批量添加灵膳
     *
     * @param request 添加请求
     * @return 自定义响应
     */
    @PostMapping("/add/seasoning/batch")
    public BaseResponse<String> addSeasoningByBatch(@RequestBody SeasoningBatchAddRequest request) {
        // 校验参数
        ParamsCheckUtils.checkObj(request);
        dishService.addSeasoningByBatch(request);
        return ResultUtils.success("灵材批量添加成功");
    }

    /**
     * 批量获取灵材调味料
     *
     * @return 凉菜调味料分页数据
     */
    @PostMapping("/fetch/spiritualSeasoning")
    public BaseResponse<PageVO<SpiritualSeasoningBase>> fetchSpiritualSeasoningByPage(@RequestBody PageRequest request) {
        // 从 redis 获取并返回
        String key = redisKey + MarketConstant.SEASONING;
        String js = redisManager.getValue(key, String.class);
        SeasoningsForRedis value = JSONUtil.toBean(js, SeasoningsForRedis.class);
        List<SpiritualSeasoningBase> seasoningBaseList = value.getSeasoningBaseList();
        if (CollUtil.isEmpty(seasoningBaseList)) {
            return ResultUtils.success(PageVO.emptyPage());
        }
        PageVO<SpiritualSeasoningBase> seasoningBasePageVO = PageUtils.buildPageVO(seasoningBaseList, request.getPageSize(), request.getCurrentPage());
        return ResultUtils.success(seasoningBasePageVO);
    }
}
