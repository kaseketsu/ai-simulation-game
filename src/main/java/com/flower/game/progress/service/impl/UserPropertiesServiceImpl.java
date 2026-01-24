package com.flower.game.progress.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flower.game.user.dao.UserPropertiesMapper;
import com.flower.game.progress.model.entity.UserProperties;
import com.flower.game.progress.service.IUserPropertiesService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户相关的属性 服务实现类
 * </p>
 *
 * @author F1ower
 * @since 2025-12-28
 */
@Service
public class UserPropertiesServiceImpl extends ServiceImpl<UserPropertiesMapper, UserProperties> implements IUserPropertiesService {

}
