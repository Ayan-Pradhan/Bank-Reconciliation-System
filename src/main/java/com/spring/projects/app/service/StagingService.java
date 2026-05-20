package com.spring.projects.app.service;

import org.springframework.stereotype.Service;

import com.spring.projects.app.repository.StagingRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StagingService {

	private final StagingRepository stagingRepo;
	
	public String insertRawBankRecords() {
		return stagingRepo.insertStagingBankRecords();
	}
	
	public String insertRawUserRecords() {
		return stagingRepo.insertStagingUserRecords();
	}
	
	public void clearRawRecords() {
		stagingRepo.clearStagingTables();
	}
}
