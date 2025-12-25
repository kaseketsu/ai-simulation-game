package common.aop;

import common.Exceptions.BusinessException;
import common.Exceptions.ErrorCode;
import common.baseEntities.BaseResponse;
import common.utils.ResultUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常拦截器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     *
     * @param e 业务异常
     * @return 通用回复（业务异常）
     */
    @ExceptionHandler(BusinessException.class)
    public <T> BaseResponse<T> handleBusinessException(BusinessException e) {
        return ResultUtils.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理通用异常
     *
     * @param e 通用异常
     * @return 通用回复（通用异常）
     */
    @ExceptionHandler(Exception.class)
    public <T> BaseResponse<T> handleException(Exception e) {
        return ResultUtils.fail(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }
}
