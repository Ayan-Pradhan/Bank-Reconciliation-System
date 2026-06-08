package com.spring.projects.app.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.spring.projects.app.exception.LedgerNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FileLoaderService {
	
	public Resource getBankLedger() throws LedgerNotFoundException{
		ClassPathResource bankResource = new ClassPathResource("ledgers/bank_records.csv");
		if(bankResource.exists())
			return bankResource;
		
		throw new LedgerNotFoundException("Bank ledger not found to perform job");
	}
	
	public Resource getUserLedger() throws LedgerNotFoundException{
		ClassPathResource userResource = new ClassPathResource("ledgers/user_records.csv");
		if(userResource.exists())
			return userResource;
		
		throw new LedgerNotFoundException("User ledger not found to perform job");
	}

}
