package cc.gfc.mvc.framework.annotation;

import java.lang.annotation.*;

/**
 * @author xiaoguo
 * @ClassName: MyRequestParam
 * @Description:
 * @CreatedBy: fcguo
 * @CreatedAt: 6/19/19 4:31 PM
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface MyRequestParam {
    String value();
}
