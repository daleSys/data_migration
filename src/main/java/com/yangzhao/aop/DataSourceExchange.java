package com.yangzhao.aop;

import com.yangzhao.datasources.DataSourceContextHolder;
import com.yangzhao.datasources.Read;
import com.yangzhao.datasources.Write;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by yangzhao on 17/2/7.
 */
@Component
@Aspect
public class DataSourceExchange {

    @AfterReturning("anyMethod()")
    public void afterReturning(){
        DataSourceContextHolder.clearDataSource();
    }

    @Pointcut("execution(* com.yangzhao.dao..*.*(..))")
    private void anyMethod() {
    }

    @Before("anyMethod()")
    public void before(JoinPoint joinPoint){
        Object target = joinPoint.getTarget();
        String name = joinPoint.getSignature().getName();
        Method[] declaredMethods = target.getClass().getDeclaredMethods();
        Optional<Method> methodOptional = Arrays.stream(declaredMethods).filter((method) -> {
            if (method.getName().equals(name)) {
                return true;
            }
            return false;
        }).findFirst();
        Method method = methodOptional.get();
        method.getParameterTypes();
    }
}
