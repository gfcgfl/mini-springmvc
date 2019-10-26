package cc.gfc.mvc.framework.annotation;

import java.lang.annotation.*;

/**
 * @author xiaoguo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)  //在这里规定 为了简单，此注解只能添加在 方法上，且只支持一个uri
public @interface MyRequestMapping {
    String value();
}
