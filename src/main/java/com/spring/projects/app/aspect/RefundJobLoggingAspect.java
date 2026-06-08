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
public class RefundJobLoggingAspect {

	private Logger logger = LoggerFactory.getLogger(RefundJobLoggingAspect.class);
	
	@Before("com.spring.projects.app.configuration.ReconciliationServiceGlobalPointcutConfig.refundServiceBatchConfig()")
	public void logBatchRefundMethods(JoinPoint joinPoint) {
		logger.info("Batch refund process started for transaction with mismatched records");
	}
	
	@Around("com.spring.projects.app.configuration.ReconciliationServiceGlobalPointcutConfig.refundServiceBatchConfig()")
	public Object logBatchRefundException(ProceedingJoinPoint joinPoint) {
		try {
			return joinPoint.proceed();
		} catch (Throwable ex) {
			logger.error("Batch refund process failed: {}", ex.getMessage());
			return null;
		}
	}
	
	@AfterReturning(pointcut="com.spring.projects.app.configuration.ReconciliationServiceGlobalPointcutConfig.refundServiceBatchConfig()",returning = "resultValue" )
	public void logBatchRefundMethods(JoinPoint joinPoint,Object resultValue) {
		logger.info("Batch refund completed successfully");
	}
	
	@Before("com.spring.projects.app.configuration.ReconciliationServiceGlobalPointcutConfig.refundServiceManualConfig()")
	public void logManualRefundMethods(JoinPoint joinPoint) {
		logger.info("Manual refund process started for transaction with mismatched record");
	}
	
	@Around("com.spring.projects.app.configuration.ReconciliationServiceGlobalPointcutConfig.refundServiceManualConfig()")
	public Object logManualRefundException(ProceedingJoinPoint joinPoint) {
		try {
			return joinPoint.proceed();
		} catch (Throwable ex) {
			logger.error("Manual refund process failed: {}", ex.getMessage());
			return null;
		}
	}
	
	@AfterReturning(pointcut="com.spring.projects.app.configuration.ReconciliationServiceGlobalPointcutConfig.refundServiceManualConfig()",returning = "resultValue" )
	public void logManualRefundMethod(JoinPoint joinPoint,Object resultValue) {
		logger.info("Refund completed successfully");
	}
	
}
