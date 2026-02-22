package com.flower.game.manage.models.entity;

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
 * 修士基础信息表
 * </p>
 *
 * @author F1ower
 * @since 2026-02-22
 */
@Getter
@Setter
@ToString
@TableName("cultivation_base")
public class CultivationBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 修士唯一ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户唯一ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 修士基础属性
     */
    @TableField("properties")
    private String properties;

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
     * 是否删除 (0 - 否, 1 - 是)
     */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}
