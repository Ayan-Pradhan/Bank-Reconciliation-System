package com.spring.projects.app.service;

import org.springframework.stereotype.Service;

import com.spring.projects.app.exception.RecordNotFoundException;
import com.spring.projects.app.repository.UserRecordRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ArchiveService {

	private final UserRecordRepository userRepo;
	
	public long archive() throws RecordNotFoundException {
		long result = userRepo.insert();
		if(result == 0) 
			throw new RecordNotFoundException("No record available in the ledger");
		
		return result;
	}
	
}
