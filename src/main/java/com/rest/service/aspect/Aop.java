package com.rest.service.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class Aop {

	
	@Before("execution(* com.rest.controller.TokenController.*(..))")
    public void logBeforeAllMethods(JoinPoint joinPoint) { 
		System.out.println(joinPoint);
	}
 
    @Before("execution(* com.rest.controller.TokenController.*(..))")
    public void logBeforeGetEmployee(JoinPoint joinPoint) { 
    	System.out.println(joinPoint);
    }
}
