package com.spring.projects.app.service;

import org.springframework.stereotype.Service;

import com.spring.projects.app.exception.RecordNotFoundException;
import com.spring.projects.app.repository.BankRecordRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BankRecordService {
	
	private final BankRecordRepository bankRepo;
	
	public long insertMatched() throws RecordNotFoundException {
		long result = bankRepo.insert();
		if(result == 0)
			throw new RecordNotFoundException("No inconsistent record found in the ledger");
		
		return result;
	}
}
