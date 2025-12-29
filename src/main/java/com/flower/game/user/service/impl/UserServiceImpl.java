package com.flower.game.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flower.game.user.dao.UserMapper;
import com.flower.game.user.models.entity.User;
import com.flower.game.user.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户基础表 服务实现类
 * </p>
 *
 * @author F1ower
 * @since 2025-12-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
