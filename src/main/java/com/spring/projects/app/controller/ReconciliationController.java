package com.spring.projects.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.projects.app.dto.RefundDetails;
import com.spring.projects.app.dto.Response;
import com.spring.projects.app.service.ReconciliationBatchService;
import com.spring.projects.app.service.RefundService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/service")
public class ReconciliationController {
	
	private final ReconciliationBatchService batchService;
	private final RefundService refundService;
	
	@GetMapping("/start-service")
	public ResponseEntity<Response> startService() {
		return ResponseEntity.ok(batchService.startReconciliationService());
	}
	
	@PostMapping("/initiate-refund")
	public ResponseEntity<Response> initiateRefund(@RequestBody RefundDetails refundDetails) {
		return ResponseEntity.ok(refundService.initiateManualRefund(refundDetails));
	}

	
	
}
