package com.flower.game.user.service.impl;

import generator.model.UserProperties;
import generator.dao.UserPropertiesMapper;
import generator.service.IUserPropertiesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
