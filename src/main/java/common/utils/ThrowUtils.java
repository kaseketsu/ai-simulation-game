package common.utils;

import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;

/**
 * 抛异常专用工具类
 *
 * @author F1ower
 * @since 2026-1-2
 */
public class ThrowUtils {

    public static  void throwIf(boolean condition, String code, String message) {
        if (condition) {
            throw new BusinessException(code, message);
        }
    }

    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, errorCode.getCode(), message);
    }

    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, errorCode.getCode(), errorCode.getMessage());
    }
}
