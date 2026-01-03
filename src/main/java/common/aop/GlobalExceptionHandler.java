package common.aop;

import common.exceptions.BusinessException;
import common.exceptions.ErrorCode;
import common.baseEntities.BaseResponse;
import common.utils.ResultUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常拦截器（暂时不需要）
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
        return ResultUtils.fail(ErrorCode.SYSTEM_ERROR);
    }

    /**
     * 对不合法参数进行拦截
     *
     * @param e 异常
     * @return 错误信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<?> handleValid(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .orElse("参数不合法");
        return ResultUtils.fail(ErrorCode.PARAM_ERROR, msg);
    }
}
