package com.app.myproject.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TimeLogAspect {

    private Logger logger = LoggerFactory.getLogger("Time Logger");

//    @Around("@annotation(com.app.myproject.annotations.TimeLog)")
//    public void logTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        long start = System.currentTimeMillis();
//        proceedingJoinPoint.proceed();
//        long end = System.currentTimeMillis();
//        long result = (end-start)/1000;
//        logger.info(String.valueOf(result));
//    }


    @Around("execution(* com.app.myproject.controller.*.*(..))")
    public void log(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        joinPoint.proceed();
        long end = System.currentTimeMillis();
        long result = (end-start)/1000;
        logger.info(String.valueOf(result));
    }




}
