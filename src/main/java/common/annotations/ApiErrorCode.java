package common.annotations;

import common.exceptions.ErrorCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 为每个 controller 定义其需要抛的 errorCode
 *
 * @author F1ower
 * @since 2026-1-3
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrorCode {

    /**
     * 自定义错误码
     *
     * @return errorCode
     */
    ErrorCode value();
}
