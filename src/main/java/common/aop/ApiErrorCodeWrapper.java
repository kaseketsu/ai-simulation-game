package common.aop;

import common.annotations.ApiErrorCode;
import common.exceptions.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 对内部抛出异常封装, 避免暴露过多信息
 *
 * @author F1ower
 * @since 2026-1-3
 */
@Aspect
@Component
public class ApiErrorCodeWrapper {

    /**
     * 对异常的再封装
     *
     * @param joinPoint    链接点
     * @param apiErrorCode 错误码注解
     * @return businessException
     */
    @Around("@annotation(apiErrorCode)")
    public Object wrap(ProceedingJoinPoint joinPoint, ApiErrorCode apiErrorCode) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (BusinessException e) {
            throw e;
        } catch (Throwable e) {
            // 封装为 businessException
            throw new BusinessException(apiErrorCode.value());
        }
    }
}
