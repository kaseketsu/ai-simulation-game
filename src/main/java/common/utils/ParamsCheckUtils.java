package common.utils;

import common.annotations.ExceptionLog;
import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 参数判空工具类
 *
 * @author F1ower
 * @since 2026-1-1
 */
@Slf4j
public class ParamsCheckUtils {

    /**
     * 对于空值进行统一校验和抛异常
     */
    @ExceptionLog("空值错误")
    public static void checkAll(Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "参数不能为空");
            }
        }
    }

    /**
     * 判断当前参数是否是指定类型
     *
     * @param object 参数
     * @param type   类型
     * @return Y / N
     */
    public static <T> boolean isSpecifiedType(Object object, Class<T> type) {
        if (Objects.isNull(object) || Objects.isNull(type) || !type.isInstance(object)) {
            return false;
        }
        return true;
    }

    /**
     * 判断当前参数是否是指定类型
     *
     * @param object 参数
     * @param type   类型
     * @return Y
     */
    public static <T> boolean isSpecifiedTypeThrow(Object object, Class<T> type) {
        if (Objects.isNull(object) || Objects.isNull(type) || !type.isInstance(object)) {
            throw new BusinessException(ErrorCode.TYPE_TRANSFER_ERROR);
        }
        return true;
    }
}
