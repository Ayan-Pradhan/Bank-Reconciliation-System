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
public class ArchiveJobLoggingAspect {
	
	private Logger logger = LoggerFactory.getLogger(ArchiveJobLoggingAspect.class);
	
	@Before("com.spring.projects.app.configuration.ReconciliationServiceGlobalPointcutConfig.archiveServiceConfig()")
	public void logUserRecordService(JoinPoint joinPoint) {
		logger.info("Archiving process started");
	}
	
	@Around("com.spring.projects.app.configuration.ReconciliationServiceGlobalPointcutConfig.archiveServiceConfig()")
	public Object logUserRecordServiceException(ProceedingJoinPoint joinPoint) {		
		try {
			return joinPoint.proceed();
		} catch (Throwable ex) {
			logger.error("Archiving process failed: {}", ex.getMessage());
			return 0;
		}
	}
	
	@AfterReturning(pointcut="com.spring.projects.app.configuration.ReconciliationServiceGlobalPointcutConfig.archiveServiceConfig()",returning="resultValue")
	public void logUserRecordServiceAfterCompletion(JoinPoint joinPoint, Object resultValue) {
		logger.info("Archiving process completed");
	}

}
