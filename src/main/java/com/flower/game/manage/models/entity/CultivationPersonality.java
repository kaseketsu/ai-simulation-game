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
 * 修士性格信息
 * </p>
 *
 * @author F1ower
 * @since 2026-02-22
 */
@Getter
@Setter
@ToString
@TableName("cultivation_personality")
public class CultivationPersonality implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 修士ID
     */
    @TableField("cultivation_id")
    private Long cultivationId;

    /**
     * 性情类型
     */
    @TableField("temperament")
    private Byte temperament;

    /**
     * 说话风格
     */
    @TableField("speech_style")
    private Byte speechStyle;

    /**
     * 性格标签数组
     */
    @TableField("traits")
    private String traits;

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
