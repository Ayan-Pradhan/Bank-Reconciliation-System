package com.spring.projects.app.exception;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.spring.projects.app.constant.ResponseStatusCode;
import com.spring.projects.app.dto.ErrorResponse;

@RestControllerAdvice
public class ReconciliationExceptionHandler extends ResponseEntityExceptionHandler {
	
	private Logger logger = LoggerFactory.getLogger(ReconciliationExceptionHandler.class);
	
	@ExceptionHandler({
			JobInstanceAlreadyCompleteException.class,
			JobExecutionAlreadyRunningException.class,
			InvalidJobParametersException.class,
			JobRestartException.class,
			BatchJobUnsuccessfulException.class
	})
	public ResponseEntity<ErrorResponse> handleBatchJobUnsuccessfulException(Exception ex) {
		logger.error("Batch job unsuccessful: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse(ResponseStatusCode.FAILED, ex.getMessage(), LocalDateTime.now()));
	}
	
	@ExceptionHandler(RecordNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleRecordNotFoundException(RecordNotFoundException ex) {
		logger.error("Batch job unsuccessful: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponse(ResponseStatusCode.FAILED, ex.getMessage(), LocalDateTime.now()));
	}
	
	@ExceptionHandler(RefundUnsuccessfulException.class)
	public ResponseEntity<ErrorResponse> handleRefundUnsuccessfulException(RefundUnsuccessfulException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(ResponseStatusCode.FAILED, ex.getMessage(), LocalDateTime.now()));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		logger.error("Something went wrong: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse(ResponseStatusCode.FAILED, ex.getMessage(), LocalDateTime.now()));
	}
	
}
