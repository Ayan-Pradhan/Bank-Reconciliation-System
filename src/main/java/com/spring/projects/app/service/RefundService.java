package com.spring.projects.app.service;

import org.springframework.stereotype.Service;

import com.spring.projects.app.constant.RefundStatus;
import com.spring.projects.app.constant.ResponseStatusCode;
import com.spring.projects.app.dto.RefundDetails;
import com.spring.projects.app.dto.Response;
import com.spring.projects.app.exception.RefundUnsuccessfulException;
import com.spring.projects.app.exception.ReportModificationFaliureException;
import com.spring.projects.app.repository.RefundRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RefundService {
	
	private final RefundRepository refundRepo;
	private final ReportService reportService;
	
	public void initiateBatchRefund() throws RefundUnsuccessfulException, ReportModificationFaliureException {
		if(refundRepo.insert() > 0 && refundRepo.updateBatch(RefundStatus.SUCCESS) > 0) 
			if(reportService.updateReportBatch(RefundStatus.SUCCESS) > 0) 
				return;
			
		throw new RefundUnsuccessfulException("No records found to initiate batch refund");
	}
	
	public Response initiateManualRefund(RefundDetails refundDetails) throws RefundUnsuccessfulException, ReportModificationFaliureException {
		if(refundRepo.update(refundDetails, RefundStatus.SUCCESS) > 0) {
			long code = reportService.updateReportManual(refundDetails, RefundStatus.SUCCESS);
			return new Response(ResponseStatusCode.SUCCESS,"Refund is successful with exit code  " + code);
		}
        throw new RefundUnsuccessfulException("Refund unsucccessful for Txn Id: " + refundDetails.txnId());
	}
	
}
 