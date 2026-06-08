package com.spring.projects.app.service;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.stereotype.Service;

import com.spring.projects.app.constant.ResponseStatusCode;
import com.spring.projects.app.dto.Response;
import com.spring.projects.app.exception.BatchJobUnsuccessfulException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReconciliationBatchService {

	private final JobOperator operator;
	private final Job reconciliationJob;

	public Response startReconciliationService() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, InvalidJobParametersException, JobRestartException, BatchJobUnsuccessfulException {
			final JobParameters jobParameters = new JobParametersBuilder()
					.addLong("startAt", System.currentTimeMillis())
					.toJobParameters();
		
			JobExecution job = operator.start(reconciliationJob, jobParameters);
			if(job.getAllFailureExceptions().isEmpty())
				return new Response(ResponseStatusCode.SUCCESS, "Reconciliation job successful");
			
			throw new BatchJobUnsuccessfulException("Reconciliation job failed");	
			
	}

}
