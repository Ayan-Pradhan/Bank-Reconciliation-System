package com.spring.projects.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.projects.app.dto.RefundDetails;
import com.spring.projects.app.dto.Response;
import com.spring.projects.app.exception.RefundUnsuccessfulException;
import com.spring.projects.app.exception.ReportModificationFaliureException;
import com.spring.projects.app.service.RefundService;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/service/refund")
public class RefundController {
	
	private final RefundService refundService;

	@PostMapping("/initiate-refund")
	public ResponseEntity<Response> initiateRefund(@RequestBody RefundDetails refundDetails) throws RefundUnsuccessfulException, ReportModificationFaliureException {
		return ResponseEntity.ok(refundService.initiateManualRefund(refundDetails));
	}
}
