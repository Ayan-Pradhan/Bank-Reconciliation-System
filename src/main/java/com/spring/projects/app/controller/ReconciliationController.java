package com.spring.projects.app.controller;

import java.io.File;

import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.projects.app.dto.Response;
import com.spring.projects.app.exception.BatchJobUnsuccessfulException;
import com.spring.projects.app.service.ReconciliationBatchService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/service/batch-job")
public class ReconciliationController {
	
	private final ReconciliationBatchService batchService;
	
	@GetMapping("/start-service")
	public ResponseEntity<Response> startService() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, InvalidJobParametersException, JobRestartException, BatchJobUnsuccessfulException {
		return ResponseEntity.ok(batchService.startReconciliationService());
	}
	
	@PostMapping("/upload-ledger")
	public ResponseEntity<Response> uploadFile(@RequestBody File file){
		return null;
	}
	
	

	
}
