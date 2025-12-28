package com.flower.game.user.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户相关的属性
 * </p>
 *
 * @author F1ower
 * @since 2025-12-28
 */
@Getter
@Setter
@ToString
@TableName("user_properties")
public class UserProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 0 - 普通用户; 1 - vip; 9 - 管理员
     */
    @TableField("user_role")
    private String userRole;

    /**
     * 0 - 饮品, 1 - 主食
     */
    @TableField("store_type")
    private String storeType;

    /**
     * 眼光
     */
    @TableField("sense")
    private Integer sense;

    /**
     * 口才
     */
    @TableField("speaking_skill")
    private Integer speakingSkill;

    /**
     * 厨艺
     */
    @TableField("cooking_skill")
    private Integer cookingSkill;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField("creator")
    private String creator;

    /**
     * 修改人
     */
    @TableField("modifier")
    private String modifier;

    /**
     * 用户 id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 是否删除
     */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}
