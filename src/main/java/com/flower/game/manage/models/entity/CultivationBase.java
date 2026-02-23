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
 * 修士身份信息
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
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 道号
     */
    @TableField("dao_title")
    private String daoTitle;

    /**
     * 性别 (0-男,1-女)
     */
    @TableField("gender")
    private Byte gender;

    /**
     * 身份 (0-散修,1-内门,2-核心,3-长老)
     */
    @TableField("status")
    private Byte status;

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
     * 地域
     */
    @TableField("region")
    private Integer region;

    /**
     * 商店类型
     */
    @TableField("store_type")
    private Integer storeType;

    /**
     * 修士形象
     */
    @TableField("url")
    private String url;

    /**
     * 开场白
     */
    @TableField("opening_line")
    private String openingLine;
}
