package com.flower.game.market.models.entity;

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
 * 灵材用户库表
 * </p>
 *
 * @author F1ower
 * @since 2026-02-02
 */
@Getter
@Setter
@ToString
@TableName("spiritual_materials_repo")
public class SpiritualMaterialsRepo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 灵材唯一ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户 id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 灵材基础库 id
     */
    @TableField("base_id")
    private Long baseId;

    /**
     * 创建时间
     */
    @TableField("creat_time")
    private LocalDateTime creatTime;

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
     * 灵材数量
     */
    @TableField("count")
    private Integer count;

    /**
     * 是否删除 (0 - 否, 1 - 是)
     */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}
