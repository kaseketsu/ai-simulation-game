package common.baseEntities;

import common.exceptions.ErrorCode;
import lombok.Data;

/**
 * 基础想要实体类
 * @param <T>
 */
@Data
public class BaseResponse<T> {

    private T data;

    private String code;

    private String message;

    public BaseResponse(T data, String code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public BaseResponse(T data) {
        this(data, null, null);
    }

    public BaseResponse(T data, String message) {
        this(data, null, message);
    }

    public BaseResponse(String message) {
        this(null, null, message);
    }

    public BaseResponse(ErrorCode errorCode) {
        this(null, errorCode.getCode(), errorCode.getMessage());
    }

    public BaseResponse(ErrorCode errorCode, String message) {
        this(null, errorCode.getCode(), message);
    }
}
