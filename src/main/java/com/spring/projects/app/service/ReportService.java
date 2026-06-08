package com.spring.projects.app.service;

import org.springframework.stereotype.Service;

import com.spring.projects.app.constant.DiscrepencyType;
import com.spring.projects.app.constant.RefundStatus;
import com.spring.projects.app.dto.RefundDetails;
import com.spring.projects.app.exception.ReportModificationFaliureException;
import com.spring.projects.app.exception.ReportNotGeneratedException;
import com.spring.projects.app.repository.ReportRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReportService {

	private final ReportRepository reportRepo;
	
	public void generateReport() throws ReportNotGeneratedException {
		
		long statusMismatch = reportRepo.insertStatusMismatch();
		long amountMismatch = reportRepo.insertAmountMismatch();
		
		if(statusMismatch > 0) 
			reportRepo.updateReportForStatusMismatch("Payment status mismatched in records", DiscrepencyType.STATUS_MISMATCH, RefundStatus.INITIATED);
		
		if(amountMismatch > 0)  
			reportRepo.updateReportForAmountMismatch("Payment amount mismatched in records", DiscrepencyType.AMOUNT_MISMATCH, RefundStatus.NO_ACTION);
		
		if(statusMismatch == 0 && amountMismatch == 0)
			throw new ReportNotGeneratedException("No inconsistent records found");
	}
	
	
	public long updateReportBatch(RefundStatus status) throws ReportModificationFaliureException {
		long result = reportRepo.updateReportForInitiatedStatus(status);
		if(result > 0)
			return result;
		
		throw new ReportModificationFaliureException("No report available to modify refund status, may already be modified");
	}
	
	public long updateReportManual(RefundDetails details) throws ReportModificationFaliureException {
		long result = reportRepo.updateReportForNoActionStatus(details);
		if(result > 0)
			return result;
		
		throw new ReportModificationFaliureException("No report available to modify refund status, may already be modified");
		
	}
	
	
}
