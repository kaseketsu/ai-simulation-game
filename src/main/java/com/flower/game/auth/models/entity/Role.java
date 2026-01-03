package com.flower.game.auth.models.entity;

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
 * 角色表
 * </p>
 *
 * @author F1ower
 * @since 2025-12-28
 */
@Getter
@Setter
@ToString
@TableName("role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 0 - 普通用户; 1 - vip; 9 - 管理员
     */
    @TableField("role_code")
    private String roleCode;

    /**
     * 角色名，普通用户, vip, 管理员
     */
    @TableField("role_name")
    private String roleName;

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
     * 是否删除
     */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}
