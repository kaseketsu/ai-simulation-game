package com.flower.game.progress.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flower.game.progress.dao.PlayProgressMapper;
import com.flower.game.progress.model.entity.PlayProgress;
import com.flower.game.progress.service.IPlayProgressService;
import org.springframework.stereotype.Service;

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

}
