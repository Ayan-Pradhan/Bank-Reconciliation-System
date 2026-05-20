package com.spring.projects.app.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StagingRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final String INSERT_STAGING_BANK_RECORDS = """
			INSERT INTO staging_bank_records(id, txn_id, sender, receiver, amount, time_stamp, status)
			VALUES (:id , :txnId, :sender, :receiver, :amount, :timeStamp, :status)
			""";
	
	private static final String INSERT_STAGING_USER_RECORDS = """
			INSERT INTO staging_user_records(id, txn_id, sender, receiver, amount, time_stamp, status)
			VALUES (:id , :txnId, :sender, :receiver, :amount, :timeStamp, :status)
			""";

	private static final String TRUNCATE_STAGING_BANK_RECORDS = "TRUNCATE TABLE staging_bank_records";
	
	private static final String TRUNCATE_STAGING_USER_RECORDS = "TRUNCATE TABLE staging_user_records";
	
	public String insertStagingBankRecords() {
		return INSERT_STAGING_BANK_RECORDS;
	}
	
	public String insertStagingUserRecords() {
		return INSERT_STAGING_USER_RECORDS;
	}
	
	public void clearStagingTables() {
		jdbcTemplate.execute(TRUNCATE_STAGING_BANK_RECORDS);
		jdbcTemplate.execute(TRUNCATE_STAGING_USER_RECORDS);
	}
	
	
	

}
