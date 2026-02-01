package com.flower.game.character.service;

import com.flower.game.character.models.dto.LevelUpRequest;

import com.flower.game.progress.model.entity.PlayProgress;
import com.flower.game.progress.model.entity.UserProperties;
import com.flower.game.progress.service.IPlayProgressService;
import com.flower.game.progress.service.IUserPropertiesService;
import com.flower.game.progress.service.impl.UserPropertiesServiceImpl;

import cn.hutool.json.JSONUtil;
import common.annotations.ExceptionLog;
import common.utils.ParamsCheckUtils;
import common.utils.ThrowUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import common.exceptions.ErrorCode;

import java.util.Objects;



/**
 * 等级升级服务
 *
 * @author F1ower
 * @since 2026-1-11
 */
@Service
@Slf4j
public class LevelUpService {

    @Resource
    private IUserPropertiesService userPropertiesService;

    @Resource
    private IPlayProgressService IPlayProgressService;

    /**
     * 等级升级
     *
     * @param levelUpRequest 等级升级请求
     */
    @ExceptionLog("等级升级失败")
    @Transactional(rollbackFor = Exception.class)
    public void levelUp(final LevelUpRequest levelUpRequest) {
        // 校验请求参数
        ParamsCheckUtils.checkObj(levelUpRequest);
        // 获取游玩进度和用户 property
        final LambdaQueryWrapper<UserProperties> userPropertiesQueryWrapper = new LambdaQueryWrapper<>();
        userPropertiesQueryWrapper.eq(UserProperties::getUserId, levelUpRequest.getUserId())
                .eq(UserProperties::getIsDeleted, 0);
        final UserProperties userProperties = userPropertiesService.getOne(userPropertiesQueryWrapper);
        ThrowUtils.throwIf(Objects.isNull(userProperties), ErrorCode.NOT_FOUND_ERROR, "用户属性不存在");
        // 更新用户属性
        log.info("用户属性升级前: {}", JSONUtil.toJsonPrettyStr(userProperties));
        userProperties.setSense(userProperties.getSense() + levelUpRequest.getSenseUp());
        userProperties.setCookingSkill(userProperties.getCookingSkill() + levelUpRequest.getCookingSkillUp());
        userProperties.setSpeakingSkill(userProperties.getSpeakingSkill() + levelUpRequest.getSpeakingSkillUp());
        userPropertiesService.updateById(userProperties);
        log.info("用户属性升级后: {}", JSONUtil.toJsonPrettyStr(userProperties));
        // 获取游玩进度
        final LambdaQueryWrapper<PlayProgress> userProgressQueryWrapper = new LambdaQueryWrapper<>();
        userProgressQueryWrapper.eq(PlayProgress::getUserId, levelUpRequest.getUserId())
                .eq(PlayProgress::getIsDeleted, 0);
        final PlayProgress userProgress = IPlayProgressService.getOne(userProgressQueryWrapper);
        log.info("用户游玩进度升级前: {}", JSONUtil.toJsonPrettyStr(userProgress));
        ThrowUtils.throwIf(Objects.isNull(userProgress), ErrorCode.NOT_FOUND_ERROR, "用户游玩进度不存在");
        // 更新游玩进度等级
        userProgress.setStoreLevel(userProgress.getStoreLevel() + levelUpRequest.getLevelUpCount());
        log.info("用户游玩进度升级后: {}", JSONUtil.toJsonPrettyStr(userProgress));
        IPlayProgressService.updateById(userProgress);
    }
}
