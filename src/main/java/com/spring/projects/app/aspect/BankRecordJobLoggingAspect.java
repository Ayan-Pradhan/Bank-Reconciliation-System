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
public class BankRecordJobLoggingAspect {
	
	private Logger logger = LoggerFactory.getLogger(BankRecordJobLoggingAspect.class);
	
	@Before("com.spring.projects.app.configuration.ReconciliationServiceGlobalPointcutConfig.bankRecordServiceConfig()")
	public void logBankRecordService(JoinPoint joinPoint) {
		logger.info("Matched record writing process started");
	}
	
	@Around("com.spring.projects.app.configuration.ReconciliationServiceGlobalPointcutConfig.bankRecordServiceConfig()")
	public Object logBankRecordServiceException(ProceedingJoinPoint joinPoint) {		
		try {
			return joinPoint.proceed();
		} catch (Throwable ex) {
			logger.error("Matched record writing process failed: {}", ex.getMessage());
			return 0;
		}
	}
	
	@AfterReturning(pointcut="com.spring.projects.app.configuration.ReconciliationServiceGlobalPointcutConfig.bankRecordServiceConfig()",returning="resultValue")
	public void logBankRecordServiceAfterCompletion(JoinPoint joinPoint, Object resultValue) {
		logger.info("Matched record writing process completed");
	}

}
