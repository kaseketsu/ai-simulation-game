package com.flower.game.base.models.entity;

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
 * 灵膳配方基础信息表
 * </p>
 *
 * @author F1ower
 * @since 2026-02-21
 */
@Getter
@Setter
@ToString
@TableName("spiritual_recipe_base")
public class SpiritualRecipeBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 调味料唯一ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 主食材
     */
    @TableField("main_ingredient")
    private String mainIngredient;

    /**
     * 辅食材
     */
    @TableField("side_ingredient")
    private String sideIngredient;

    /**
     * 调味料
     */
    @TableField("seasoning")
    private String seasoning;

    /**
     * ai 生成结果
     */
    @TableField("response")
    private String response;

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
