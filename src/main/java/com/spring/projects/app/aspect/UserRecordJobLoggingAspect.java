package com.spring.projects.app.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class UserRecordJobLoggingAspect {
	
	private Logger logger = LoggerFactory.getLogger(UserRecordJobLoggingAspect.class);
	
	@Before("com.spring.project.app.aspect.userRecordServiceConfig")
	public void logUserRecordService(JoinPoint joinPoint) {
		logger.info("Record writing process started");
	}
	
	@Around("com.spring.project.app.aspect.userRecordServiceConfig")
	public Object logUserRecordServiceException(ProceedingJoinPoint joinPoint) {		
		try {
			return joinPoint.proceed();
		} catch (Throwable ex) {
			logger.error("Record writing process failed: {}", ex.getMessage());
			return 0;
		}
	}
	
	@AfterReturning(pointcut="com.spring.project.app.aspect.userRecordServiceConfig",returning="resultValue")
	public void logUserRecordServiceAfterCompletion(JoinPoint joinPoint, Object resultValue) {
		logger.info("Record writing process completed");
	}

}
