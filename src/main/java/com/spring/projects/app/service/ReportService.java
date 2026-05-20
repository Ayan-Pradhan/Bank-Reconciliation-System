package com.spring.projects.app.service;

import org.springframework.stereotype.Service;

import com.spring.projects.app.constant.DiscrepencyType;
import com.spring.projects.app.constant.RefundStatus;
import com.spring.projects.app.entity.Report;
import com.spring.projects.app.repository.ReportRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReportService {

	private final ReportRepository reportRepo;
	
	public void generateReport() {
		if(reportRepo.insertStatusMismatch() > 0) {
			reportRepo.updateReportForStatusMismatch("Payment status mismatched in records", DiscrepencyType.STATUS_MISMATCH, RefundStatus.INITIATED);
		}
		if(reportRepo.insertAmountMismatch() > 0) {
			reportRepo.updateReportForAmountMismatch("Payment amount mismatched in records", DiscrepencyType.STATUS_MISMATCH, RefundStatus.NO_ACTION);
		}
	}
	
	public void updateReportBatch(RefundStatus status) {
		reportRepo.updateReportForInitiatedRefund(status);
	}
	
	public void updateReportManual(Report report) {
		reportRepo.updateReportForNoActionRefund(report);
	}
	
	
}
