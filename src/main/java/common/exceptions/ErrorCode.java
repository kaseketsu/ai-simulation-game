package common.exceptions;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

/**
 * 错误枚举: 六位
 * 前两位: 模块 99 - 通用 / 01 - 用户
 * 中间两位: 错误类型 00 - 成功 / 01 - 参数 / 02 - 系统 / 03 - 业务 / 04 - 权限
 * 最后两位: 具体错误 可自定义
 *
 * @author F1ower
 * @since 2026-12-30
 */
@Getter
public enum ErrorCode {

    // ======= 通用错误 =======
    SUCCESS("990000", "操作成功"),
    PARAM_ERROR("990100", "参数不合法"),
    OPERATION_ERROR("990201", "操作失败"),
    AUTHORIZE_ERROR("990400", "无权限"),
    SYSTEM_ERROR("990200", "系统异常"),
    PARSE_ERROR("990202", "解析失败"),
    FILTER_ERROR("990300", "过滤失败"),
    ILLEGAL_USER("990301", "非法用户"),
    GENERATE_ERROR("990302", "生成失败"),
    EXPIRE_ERROR("990303", "已过期"),
    NOT_FOUND_ERROR("990304", "未找到"),
    TYPE_TRANSFER_ERROR("990101", "类型转换出错"),
    ALREADY_EXIST_ERROR("990203", "已存在"),
    SYS_CODE_ADD_ERROR("990305", "码值添加失败"),
    SAVE_ERROR("990306", "保存失败"),
    QUERY_ERROR("990307", "查询失败"),

    // ======= 用户模块错误 =======
    LOGIN_ERROR("010300", "登录失败"),
    REFRESH_ERROR("010301", "获取新 token 失败"),
    REGISTER_ERROR("010302", "注册失败"),
    LOG_OUT_ERROR("010303", "下线失败"),
    INVALIDATE_ERROR("010304", "下线失败"),

    // ======= 权限模块错误 =======
    ROLE_ADD_ERROR("020200", "角色添加失败"),

    // ======= 游戏入口模块错误 =======
    GAME_INIT_ERROR("030300", "游戏初始化失败"),

    // ======= 游戏角色模块错误 =======
    CHARACTER_LEVEL_UP_ERROR("040300", "角色等级升级失败");

    /**
     * 错误码
     */
    private final String code;

    /**
     * 错误信息
     */
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据值获取对应枚举
     *
     * @param value 枚举值
     * @return 枚举
     */
    public static ErrorCode getErrorCodeByValue(String value) {
        if (StrUtil.isBlank(value)) {
            return null;
        }
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getCode().equals(value)) {
                return errorCode;
            }
        }
        return null;
    }

}
