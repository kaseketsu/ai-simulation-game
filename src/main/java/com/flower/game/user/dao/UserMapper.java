package com.flower.game.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flower.game.user.models.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户基础表 Mapper 接口
 * </p>
 *
 * @author F1ower
 * @since 2025-12-28
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据账号获取用户详情
     *
     * @param userAccount 账号
     * @return 用户详情
     */
    public User selectByUserAccount(@Param("userAccount") String userAccount);
}
