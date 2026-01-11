package com.flower.game.entrance.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flower.game.entrance.models.dto.GameInitRequest;
import com.flower.game.entrance.models.vo.GameInitStateVO;
import com.flower.game.progress.model.entity.PlayProgress;
import com.flower.game.progress.model.entity.UserProperties;
import com.flower.game.progress.service.IPlayProgressService;
import com.flower.game.progress.service.IUserPropertiesService;
import common.annotations.ExceptionLog;
import common.utils.ParamsCheckUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Objects;

/**
 * 游戏初始化业务类
 *
 * @author F1ower
 * @since 2026-1-11
 */
@Service
@Slf4j
public class GameEntranceService {

    @Resource
    private IPlayProgressService playProgressService;

    @Resource
    private IUserPropertiesService userPropertiesService;

    /**
     * 游戏初始化
     *
     * @param initRequest 初始化请求
     * @return 初始化值
     */
    @ExceptionLog("游戏初始化失败")
    @Transactional(rollbackFor = Exception.class)
    public GameInitStateVO gameStart(final GameInitRequest initRequest) {
        // 校验参数
        ParamsCheckUtils.checkObj(initRequest);
        // 判断当前用户是否有游玩进度
        LambdaQueryWrapper<PlayProgress> pqWrapper = new LambdaQueryWrapper<>();
        pqWrapper.eq(PlayProgress::getUserId, initRequest.getUserId())
                .eq(PlayProgress::getIsDeleted, 0);
        PlayProgress playProgress = playProgressService.getOne(pqWrapper);
        boolean played = Objects.nonNull(playProgress);
        // 删除游玩进度
        if (played) {
            playProgress.setIsDeleted(1);
            LambdaQueryWrapper<UserProperties> upWrapper = new LambdaQueryWrapper<>();
            upWrapper.eq(UserProperties::getUserId, initRequest.getUserId())
                    .eq(UserProperties::getIsDeleted, 0);
            UserProperties userProperties = userPropertiesService.getOne(upWrapper);
            if (Objects.nonNull(userProperties)) {
                userProperties.setIsDeleted(1);
            }
            playProgressService.updateById(playProgress);
            userPropertiesService.updateById(userProperties);
        }
        // 新建游玩进度和用户属性
        PlayProgress initPlayProgress = new PlayProgress();
        BeanUtil.copyProperties(initRequest, initPlayProgress);
        playProgress.setOpenDays(1);
        // 设置起始时间为 9:00
        playProgress.setTimePeriod(LocalTime.of(9, 0));
        playProgress.setEarnedMoney(1000L);
        playProgress.setStoreLevel(1);
        UserProperties userProperties = new UserProperties();
        BeanUtil.copyProperties(initRequest, userProperties);
        // 添加入数据库
        playProgressService.save(initPlayProgress);
        userPropertiesService.save(userProperties);
        // 返回初始化信息
        GameInitStateVO gameInitStateVO = new GameInitStateVO();
        BeanUtil.copyProperties(userProperties, gameInitStateVO);
        BeanUtil.copyProperties(initPlayProgress, gameInitStateVO);
        return gameInitStateVO;
    }
}
