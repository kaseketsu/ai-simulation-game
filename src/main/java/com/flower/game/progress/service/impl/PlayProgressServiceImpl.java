package com.flower.game.progress.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flower.game.progress.dao.PlayProgressMapper;
import com.flower.game.progress.model.dto.PlayProgressQueryRequest;
import com.flower.game.progress.model.dto.PlayProgressSaveRequest;
import com.flower.game.progress.model.entity.PlayProgress;
import com.flower.game.progress.model.entity.UserProperties;
import com.flower.game.progress.model.vo.PlayProgressVO;
import com.flower.game.progress.service.IPlayProgressService;
import com.flower.game.progress.service.IUserPropertiesService;
import common.annotations.ExceptionLog;
import common.exceptions.ErrorCode;
import common.utils.ParamsCheckUtils;
import common.utils.ThrowUtils;
import jakarta.annotation.Resource;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Objects;

/**
 * <p>
 * 用户游玩进度表 服务实现类
 * </p>
 *
 * @author F1ower
 * @since 2026-01-10
 */
@Service
public class PlayProgressServiceImpl extends ServiceImpl<PlayProgressMapper, PlayProgress> implements IPlayProgressService {

    @Resource
    private IPlayProgressService playProgressService;

    @Resource
    private IUserPropertiesService userPropertiesService;

    /**
     * 保存用户游玩进度
     *
     * @param request 保存进度请求
     */
    @Override
    @ExceptionLog("保存游玩进度失败")
    @Transactional(rollbackFor = Exception.class)
    public void savePlayProgress(final PlayProgressSaveRequest request) {
        // 参数校验
        ThrowUtils.throwIf(Objects.isNull(request), ErrorCode.PARAM_ERROR, "游玩进度保存请求参数为空");
        ParamsCheckUtils.checkObj(request);
        // 构造实体类 - playProgress
        PlayProgress playProgress = new PlayProgress();
        // 构造实体类 - userProperties
        UserProperties userProperties = new UserProperties();
        // 填充参数
        BeanUtil.copyProperties(request, playProgress);
        BeanUtil.copyProperties(request, userProperties);
        // 写入 playProgress 表
        playProgressService.save(playProgress);
        // 写入 userProperties 表
        userPropertiesService.save(userProperties);
    }

    /**
     * 查询游玩进度
     *
     * @param request 查询请求
     * @return 游玩进度展示类
     */
    @Override
    @ExceptionLog("查询游玩进度失败")
    public PlayProgressVO queryPlayProgress(final PlayProgressQueryRequest request) {
        // 参数校验
        ParamsCheckUtils.checkObj(request);
        // 构造查询条件
        Long userId = request.getUserId();
        LambdaQueryWrapper<PlayProgress> pgWrapper = new LambdaQueryWrapper<>();
        pgWrapper.eq(PlayProgress::getUserId, userId)
                .eq(PlayProgress::getIsDeleted, 0);
        PlayProgress playProgress = playProgressService.getOne(pgWrapper);
        // 如果游玩进度为空，说明是新建用户, 返回 null
        if (Objects.isNull(playProgress)) {
            return null;
        }
        // 查询用户属性
        LambdaQueryWrapper<UserProperties> upWrapper = new LambdaQueryWrapper<>();
        upWrapper.eq(UserProperties::getUserId, userId)
                .eq(UserProperties::getIsDeleted, 0);
        UserProperties userProperties = userPropertiesService.getOne(upWrapper);
        if (Objects.isNull(userProperties)) {
            return null;
        }
        // 填充属性返回
        PlayProgressVO playProgressVO = new PlayProgressVO();
        BeanUtil.copyProperties(playProgress, playProgressVO);
        BeanUtil.copyProperties(userProperties, playProgressVO);
        return playProgressVO;
    }
}
