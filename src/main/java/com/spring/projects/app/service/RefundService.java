package com.spring.projects.app.service;

import org.springframework.stereotype.Service;

import com.spring.projects.app.constant.RefundStatus;
import com.spring.projects.app.dto.RefundDetails;
import com.spring.projects.app.dto.Response;
import com.spring.projects.app.repository.RefundRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RefundService {
	
	private final RefundRepository refundRepo;
	private final ReportService reportService;

	public void initiateBatchRefund() {
		if(refundRepo.insert() > 0 && refundRepo.updateBatch(RefundStatus.SUCCESS) > 0) {
			reportService.updateReportBatch(RefundStatus.SUCCESS);
		}
	}
	
	public Response initiateManualRefund(RefundDetails refundDetails) {
		if(refundRepo.update(refundDetails) > 0) {
			reportService.updateReportManual(refundDetails);
			return new Response("SUCCESS","{}");
		}
		
		return new Response("","");
	}
	
}
