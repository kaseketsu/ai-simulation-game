package common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 码值表
 * </p>
 *
 * @author F1ower
 * @since 2026-01-03
 */
@Getter
@Setter
@ToString
@TableName("sys_code")
public class SysCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 码表 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 码值类型
     */
    @TableField("code_category")
    private String codeCategory;

    /**
     * 码值 key
     */
    @TableField("code_key")
    private String codeKey;

    /**
     * 码值 value
     */
    @TableField("code_value")
    private String codeValue;

    /**
     * 码值描述
     */
    @TableField("code_desc")
    private String codeDesc;

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
}
