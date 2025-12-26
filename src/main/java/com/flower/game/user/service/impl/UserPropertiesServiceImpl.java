package com.flower.game.user.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flower.game.user.dao.UserPropertiesMapper;
import com.flower.game.user.models.entity.UserProperties;
import com.flower.game.user.service.IUserPropertiesService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户相关的属性 服务实现类
 * </p>
 *
 * @author F1ower
 * @since 2025-12-26
 */
@Service
public class UserPropertiesServiceImpl extends ServiceImpl<UserPropertiesMapper, UserProperties> implements IUserPropertiesService {

}
