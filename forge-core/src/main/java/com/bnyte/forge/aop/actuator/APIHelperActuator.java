package com.bnyte.forge.aop.actuator;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * APIHelper动态代理的执行器
 * @auther bnyte
 * @date 2021-12-04 03:08
 * @email bnytezz@gmail.com
 */
@Aspect
@Component
public class APIHelperActuator {

    private static final Logger log = LoggerFactory.getLogger(APIHelperActuator.class);

    @Pointcut("@annotation(com.bnyte.forge.annotation.APIHelper)")
    public void pointcut(){}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        log.info("\n--------------------------------------------\n请求日志.....");
        return pjp.proceed();
    }



}
