package com.flower.game.progress.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flower.game.progress.model.dto.PlayProgressQueryRequest;
import com.flower.game.progress.model.dto.PlayProgressSaveRequest;
import com.flower.game.progress.model.entity.PlayProgress;
import com.flower.game.progress.model.vo.PlayProgressVO;
import lombok.NonNull;

/**
 * <p>
 * 用户游玩进度表 服务类
 * </p>
 *
 * @author F1ower
 * @since 2026-01-10
 */
public interface IPlayProgressService extends IService<PlayProgress> {

    /**
     * 保存用户游玩进度
     *
     * @param request 保存进度请求
     */
    void savePlayProgress(final PlayProgressSaveRequest request);

    /**
     * 查询游玩进度
     *
     * @param request 查询请求
     * @return 游玩进度展示类
     */
    PlayProgressVO queryPlayProgress(final PlayProgressQueryRequest request);
}
