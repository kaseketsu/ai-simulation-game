package com.flower.game.progress.model;

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
import java.time.LocalTime;

/**
 * <p>
 * 用户游玩进度表
 * </p>
 *
 * @author F1ower
 * @since 2026-01-10
 */
@Getter
@Setter
@ToString
@TableName("play_progress")
public class PlayProgress implements Serializable {

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
     * 经营天数
     */
    @TableField("open_days")
    private Integer openDays;

    /**
     * 营业时段
     */
    @TableField("time_period")
    private LocalTime timePeriod;

    /**
     * 营业额
     */
    @TableField("earned_money")
    private Long earnedMoney;

    /**
     * 店铺等级
     */
    @TableField("store_level")
    private Integer storeLevel;

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
