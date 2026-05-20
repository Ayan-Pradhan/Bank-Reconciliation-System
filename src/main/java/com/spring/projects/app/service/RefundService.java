package com.spring.projects.app.service;

import org.springframework.stereotype.Service;

import com.spring.projects.app.constant.RefundStatus;
import com.spring.projects.app.entity.Refund;
import com.spring.projects.app.repository.RefundRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RefundService {
	
	private final RefundRepository refundRepo;
	private final ReportService reportService;

	public void initiateRefundBatch() {
		if(refundRepo.insert() > 0 && refundRepo.updateBatch(RefundStatus.SUCCESS) > 0) {
			reportService.updateReportBatch(RefundStatus.SUCCESS);
		}
	}
	
	public void initiateRefundManual(Refund refundDetails) {
		refundRepo.update(refundDetails);
	}
	
}
