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
public class ReportJobLoggingAspect {
	
	private Logger logger = LoggerFactory.getLogger(ReportJobLoggingAspect.class);
	
	// Generation
	
	@Before("com.spring.projects.app.configuration.ReconciliationServiceGlobalPointcutConfig.reportGenerationServiceConfig()")
	public void logReportGeneration(JoinPoint joinPoint) {
		logger.info("Report generation process started for inconsistent records");
	}
	
	@Around("com.spring.projects.app.configuration.ReconciliationServiceGlobalPointcutConfig.reportGenerationServiceConfig()")
	public Object logReportGenerationException(ProceedingJoinPoint joinPoint) {
		try {
			return joinPoint.proceed();
		} 
		catch(Throwable ex) {
			logger.warn("Report not generated: {}", ex.getMessage());
			return null;
		}
	}
	
	@AfterReturning(pointcut="com.spring.projects.app.configuration.ReconciliationServiceGlobalPointcutConfig.reportGenerationServiceConfig()", returning = "resultValue")
	public void logReportGenerationAfterCompletion(JoinPoint joinPoint, Object resultValue) {
		logger.info("Report generation process successfully completed");
	}
	
	// Modifications for batch refunds
	
	@Before("com.spring.projects.app.configuration.ReconciliationServiceGlobalPointcoutConfig.reportGenerationServiceBatchConfig")
	public void logBatchReportUpdate() {
		logger.info("Updating report for already processed refunds");
	}
	
	@Around("com.spring.projects.app.configuration.ReconciliationServiceGlobalPointcoutConfig.reportGenerationServiceBatchConfig")
	public Object logBatchReportUpdateException(ProceedingJoinPoint joinPoint) {
		try {
			return joinPoint.proceed();
		}
		catch(Throwable ex) {
			logger.warn("Report modification unsuccessful: {}", ex.getMessage());
			return 0;
		}
	}
	
	@AfterReturning(pointcut="com.spring.projects.app.configuration.ReconciliationServiceGlobalPointcutConfig.reportGenerationServiceBatchConfig()", returning = "resultValue")
	public void logBatchReportUpdateAfterCompletion(JoinPoint joinPoint, Object resultValue) {
		logger.info("Report updated for already processed refunds");
	}
	
	// Modifications for manual refunds
	
	@Before("com.spring.projects.app.configuration.ReconciliationServiceGlobalPointcoutConfig.reportGenerationServiceManualConfig")
	public void logManualReportUpdate() {
		logger.info("Updating report for already processed refunds");
	}
	
	@Around("com.spring.projects.app.configuration.ReconciliationServiceGlobalPointcoutConfig.reportGenerationServiceManualConfig")
	public Object logManualReportUpdateException(ProceedingJoinPoint joinPoint) {
		try {
			return joinPoint.proceed();
		}
		catch(Throwable ex) {
			logger.warn("Report modification unsuccessful: {}", ex.getMessage());
			return 0;
		}
	}
	
	@AfterReturning(pointcut="com.spring.projects.app.configuration.ReconciliationServiceGlobalPointcutConfig.reportGenerationServiceManualConfig()", returning = "resultValue")
	public void logManualReportUpdateAfterCompletion(JoinPoint joinPoint, Object resultValue) {
		logger.info("Report updated for already processed refunds");
	}
	
	
}
