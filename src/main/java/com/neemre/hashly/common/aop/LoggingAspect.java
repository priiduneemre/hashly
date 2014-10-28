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
		String logMessage = new StringBuilder().append(clazzName).append("#").append(methodName)
				.append(getArgTypes(joinPoint.getArgs())).append(": execution started.")
				.toString();
		logger.info(logMessage);
	}

	@After("execution(* com.neemre.hashly..*.*(..))")
	public void logAllMethodExits(JoinPoint joinPoint) {
		String clazzName = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		String logMessage = new StringBuilder().append(clazzName).append("#").append(methodName)
				.append(getArgTypes(joinPoint.getArgs())).append(": execution completed.")
				.toString();
		logger.info(logMessage);
	}

	@AfterThrowing(pointcut = "execution(* com.neemre.hashly..*.*(..))", throwing = "ex")
	public void logAllExceptions(JoinPoint joinPoint, Exception ex) {
		String clazzName = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		String exceptionMessage = ex.getMessage();
		String logMessage = new StringBuilder().append(clazzName).append("#").append(methodName)
				.append(getArgTypes(joinPoint.getArgs())).append(": an exception occurred! "
						+ "(Message was: \"").append(exceptionMessage).append("\")").toString();
		logger.info(logMessage);
	}

	private String getArgTypes(Object[] args) {
		StringBuilder argTypes = new StringBuilder();
		argTypes.append("(");
		for(int i = 0; i < args.length; i++) {
			if(args[i] == null) {
				argTypes.append("null");
				continue;
			}
			argTypes.append(args[i].getClass().getSimpleName());
			if(i != (args.length - 1)) {
				argTypes.append(", ");
			}
		}
		argTypes.append(")");
		return argTypes.toString();
	}
}