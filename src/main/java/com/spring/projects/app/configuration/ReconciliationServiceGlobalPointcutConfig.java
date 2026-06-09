package com.spring.projects.app.configuration;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReconciliationServiceGlobalPointcutConfig {
	
	@Pointcut("execution(* com.spring.projects.app.service.RefundService.initiateBatchRefund())")
	public void refundServiceBatchConfig() {};
	
	@Pointcut("execution(* com.spring.projects.app.service.RefundService.initiateManualRefund())")
	public void refundServiceManualConfig() {};
	
	@Pointcut("execution(* com.spring.projects.app.service.ReportService.generateReport())")
	public void reportGenerationServiceConfig() {};
	
	@Pointcut("execution(* com.spring.projects.app.service.ReportService.updateReportBatch())")
	public void reportGenerationServiceBatchConfig() {};
	
	@Pointcut("execution(* com.spring.projects.app.service.ReportService.updateReportManual())")
	public void reportGenerationServiceManualConfig() {};
	
	@Pointcut("execution(* com.spring.projects.app.service.BankRecordService.*(..))")
	public void bankRecordServiceConfig() {};
	
	@Pointcut("execution(* com.spring.projects.app.service.UserRecordService.*(..))")
	public void userRecordServiceConfig() {};
	
	@Pointcut("execution(* com.spring.projects.app.service.ReconciliationBatchService.*(..))")
	public void reconciliationBatchServiceConfig() {};
	

}
