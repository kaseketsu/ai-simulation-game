package common.utils;

import common.exceptions.ErrorCode;
import common.baseEntities.BaseResponse;

/**
 * 结果封装工具类
 */
public class ResultUtils {

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(data, ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage());
    }

    public static <T> BaseResponse<T> success(T data, String message) {
        return new BaseResponse<>(data, message);
    }

    public static <T> BaseResponse<T> success(T data, String code, String message) {
        return new BaseResponse<>(data, code, message);
    }

    public static <T> BaseResponse<T> success(String message) {
        return new BaseResponse<>(message);
    }

    public static <T> BaseResponse<T> fail(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    public static <T> BaseResponse<T> fail(String code, String message) {
        return new BaseResponse<>(null, code, message);
    }

    public static <T> BaseResponse<T> fail(ErrorCode errorCode, String message) {
        return new BaseResponse<>(null, errorCode.getCode(), message);
    }

}
