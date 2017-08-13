package com.nowcoder.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ASUS on 2017/7/4.
 */
@Aspect
//@Component//完成初始化,打log就是面向切面编程主要体现
public class LogAspect {
    private static final Logger logger= LoggerFactory.getLogger(LogAspect.class);
    @Before("execution(* com.nowcoder.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        StringBuilder sb=new StringBuilder();
        for(Object arg:joinPoint.getArgs()){
            sb.append("arg:"+arg.toString()+" ");
        }
        logger.info("before method:"+sb.toString());
    }
    @After("execution(* com.nowcoder.controller.*Controller.*(..))")
    public void aftereMethod(JoinPoint joinPoint){
        logger.info("after method:");
    }
}
