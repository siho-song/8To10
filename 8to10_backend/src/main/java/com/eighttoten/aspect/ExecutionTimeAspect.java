package com.eighttoten.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExecutionTimeAspect {
    @Pointcut("execution(* com.eighttoten.service..*(..)) ||"
            + "execution(* com.eighttoten.controller..*(..)) ||"
            + "execution(* com.eighttoten.repository..*(..)))")
    public void businessLogicPointcut(){}

    @Around("businessLogicPointcut()")
    public Object loggingMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;
        log.trace("Method name : {} , executed in {} ms", joinPoint.getSignature(), executionTime);
        return result;
    }
}