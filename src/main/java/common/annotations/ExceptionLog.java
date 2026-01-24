package common.annotations;

import java.lang.annotation.*;

/**
 * 异常日志注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ExceptionLog {

    /**
     * 日志描述
     */
    String value() default "";
}
