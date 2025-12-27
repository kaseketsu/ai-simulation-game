package common.exceptions;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

/**
 * 错误枚举: 六位
 * 前两位: 模块 99 - 通用 / 01 - 用户
 * 中间两位: 错误类型 00 - 成功 / 01 - 参数 / 02 - 系统 / 03 - 业务 / 04 - 权限
 * 最后两位: 具体错误 可自定义
 */
@Getter
public enum ErrorCode {

    // ======= 通用错误 =======
    SUCCESS("990000", "操作成功"),
    PARAM_ERROR("990100", "参数不合法"),
    OPERATION_ERROR("990201", "操作失败"),
    AUTHORIZE_ERROR("990400", "无权限"),
    SYSTEM_ERROR("990200", "系统异常"),
    PARSE_ERROR("990202", "解析失败");

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
