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
 * 灵材基础信息表
 * </p>
 *
 * @author F1ower
 * @since 2026-02-01
 */
@Getter
@Setter
@ToString
@TableName("spiritual_materials_base")
public class SpiritualMaterialsBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 灵材唯一ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 灵材名称
     */
    @TableField("name")
    private String name;

    /**
     * 灵材类型 (0 - 灵谷, 1 - 灵蔬, 2 - 灵肉, 3 - 灵茶, 4 - 灵果, 5 - 灵酿)
     */
    @TableField("type")
    private Integer type;

    /**
     * 灵材图片 url
     */
    @TableField("url")
    private String url;

    /**
     * 店铺类型 (0 - 固本类, 1 - 淬灵类)
     */
    @TableField("store_type")
    private Integer storeType;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 组 id，同一组拥有四种品质
     */
    @TableField("group_id")
    private Long groupId;

    /**
     * 图片描述
     */
    @TableField("desc")
    private String desc;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private LocalDateTime updatedTime;

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
     * 灵材价格
     */
    @TableField("price")
    private Long price;

    /**
     * 1 - 普通，2 - 稀有，3 - 传世， 4 - 神话
     */
    @TableField("rarity")
    private Integer rarity;
}
