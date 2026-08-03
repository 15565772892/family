package org.family.core.ds;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * springboot多数据源
 *
 * 通过注解和切面实现
 * 执行具体sql or service 方法的时候，注解切入，向spring容器请求注解标签对应的数据源
 * 切面优先级比 @Transactional 优先级高
 * 为了线程安全，使用threadlocal存储数据源标识
 */

@Aspect
@Component
// 确保在 @Transactional 事务切面之前执行，否则切换无效
@Order(1)
public class DynamicDataSourceAspect {

    @Around("@annotation(dataSource)")
    public Object around(ProceedingJoinPoint point,DataSource dataSource) throws Throwable {
        String value = dataSource.value();
        if (point instanceof MethodSignature){
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            DataSource ds = method.getAnnotation(DataSource.class);
            if (ds != null){
                value = ds.value();
            }
        }

        try {
            DataSourceContextHolder.setDataSource(value);
            return point.proceed();
        }finally {
            DataSourceContextHolder.clearDataSource();
        }


    }

    @Pointcut("@annotation(org.family.core.ds.DataSource)")
    public void advice() {
    }


}
