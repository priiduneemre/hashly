package com.neemre.hashly.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component("loggingAspect")
public class LoggingAspect {

	private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
	
	
	@Before("execution(* com.neemre.hashly..*.*(..))")
	public void logAllMethodCalls(JoinPoint joinPoint) {
		String clazzName = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		logger.info("%s#%s(): execution started.", clazzName, methodName);
		System.out.println(joinPoint.getArgs());
	}
	
	@After("execution(* com.neemre.hashly..*.*(..))")
	public void logAllMethodExits(JoinPoint joinPoint) {
		String clazzName = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		logger.info("%s#%s(): execution completed.", clazzName, methodName);
		System.out.println(joinPoint.getArgs());
	}
	
	@AfterThrowing(pointcut = "execution(* com.neemre.hashly..*.*(..))", throwing = "ex")
	public void logAllExceptions(JoinPoint joinPoint, Exception ex) {
		String clazzName = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		String exceptionMsg = ex.getMessage();
		logger.error("%s#%s(): an exception occurred! (Message was: \"%s\")", clazzName, 
				methodName, exceptionMsg);
	}
}