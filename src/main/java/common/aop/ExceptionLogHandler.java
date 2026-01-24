package common.aop;

import cn.hutool.core.util.StrUtil;
import common.annotations.ExceptionLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 异常日志打印切面
 */
@Slf4j
@Aspect
@Component
public class ExceptionLogHandler {

    /**
     * 日志切点
     */
    @Pointcut("@annotation(common.annotations.ExceptionLog)")
    public void exceptionLog() {
    }

    /**
     * 异常日志记录
     *
     * @param joinPoint 链接点
     * @param ex        异常
     */
    @AfterThrowing(pointcut = "exceptionLog()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ExceptionLog annotation = method.getAnnotation(ExceptionLog.class);
        // 获取日志描述
        String desc = annotation.value();
        desc = StrUtil.isBlank(desc) ? "%s 执行失败".formatted(method.getName()) : desc;
        log.error("{}, 原因是: {}", desc, ex.getMessage(), ex);
    }
}
