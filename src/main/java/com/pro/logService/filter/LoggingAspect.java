package com.pro.logService.filter;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before("execution(* com.pro.bankService.controller..*(..))")
    public void logBeforeController(JoinPoint joinPoint) {
        log.info("[Controller] {}", joinPoint.getSignature().toShortString());
    }

    @Before("execution(* com.pro.bankService.service..*(..))")
    public void logBeforeService(JoinPoint joinPoint) {
        log.info("[Service] {}", joinPoint.getSignature().toShortString());
    }

    @Before("execution(* com.pro.bankService.repository..*(..))")
    public void logBeforeRepository(JoinPoint joinPoint) {
        log.info("[Repository] {}", joinPoint.getSignature().toShortString());
    }
}