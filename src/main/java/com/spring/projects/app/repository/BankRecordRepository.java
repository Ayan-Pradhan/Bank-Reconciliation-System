package com.spring.projects.app.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BankRecordRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final String INSERT_INTO_BANK_RECORD = """
			INSERT INTO bank_record (id, txn_id, sender, receiver, amount, time_stamp, status)
	        SELECT a.id, a.txn_id, a.sender, a.receiver, a.amount, a.time_stamp, a.status
	        FROM staging_bank_records a
	        LEFT JOIN staging_user_records b ON a.txn_id = b.txn_id
	        WHERE a.status = b.status AND a.amount = b.amount
			""";
	
	public void add() {
		jdbcTemplate.update(INSERT_INTO_BANK_RECORD);
	}
	
}
