package org.family.core.ds;

import java.lang.annotation.*;

/**
 * 定义一个注解，用来打在 Mapper 接口或 Service 方法上。
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {

    String value();

}
