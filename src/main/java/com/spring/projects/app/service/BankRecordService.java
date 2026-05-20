package com.spring.projects.app.service;

import org.springframework.stereotype.Service;

import com.spring.projects.app.repository.BankRecordRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BankRecordService {
	
	private final BankRecordRepository bankRepo;
	
	public void insertMatched() {
		bankRepo.add();
	}
}
