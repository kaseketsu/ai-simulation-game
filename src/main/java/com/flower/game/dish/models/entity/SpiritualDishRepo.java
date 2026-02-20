package com.flower.game.dish.models.entity;

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
 * 灵膳基础信息表
 * </p>
 *
 * @author F1ower
 * @since 2026-02-20
 */
@Getter
@Setter
@ToString
@TableName("spiritual_dish_repo")
public class SpiritualDishRepo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 调味料唯一ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户唯一ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 灵膳名称
     */
    @TableField("name")
    private String name;

    /**
     * 灵膳图片 url
     */
    @TableField("url")
    private String url;

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

    /**
     * 灵膳价格
     */
    @TableField("price")
    private Long price;
}
