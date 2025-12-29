package com.flower.game.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flower.game.user.dao.UserDetailMapper;
import com.flower.game.user.models.entity.UserDetail;
import com.flower.game.user.service.IUserDetailService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户详情表 服务实现类
 * </p>
 *
 * @author F1ower
 * @since 2025-12-28
 */
@Service
public class UserDetailServiceImpl extends ServiceImpl<UserDetailMapper, UserDetail> implements IUserDetailService {

}
