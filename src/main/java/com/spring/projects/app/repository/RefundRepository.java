package com.spring.projects.app.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.spring.projects.app.constant.ProcessedStatus;
import com.spring.projects.app.constant.RefundStatus;
import com.spring.projects.app.dto.RefundDetails;

@Repository
public class RefundRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate; 
	
	private static final String INSERT_REFUND_DETAILS = """
			INSERT INTO refund (txn_id, amount, cause, refund_status)
			SELECT r.txn_id, r.bank_amount, r.discrepency_type, r.refund_status FROM report r
			""";
	
	private static final String UPDATE_STATUS_MISMATCH = """
			UPDATE refund SET refund_status = ?, time_stamp = ?, is_processed = ?
			WHERE is_processed = 'FALSE' AND cause = 'STATUS_MISMATCH'
			""";
	
	private static final String UPDATE_AMOUNT_MISMATCH = """
			UPDATE refund SET amount = ?, refund_status = ?, time_stamp = ?, is_processed = ?
			WHERE txn_id = ? AND is_processed = 'FALSE' AND cause = 'AMOUNT_MISMATCH'
			""";
	
	/**
	 * Methods to process the refund
	 * insert() method is used to insert records into the refund table.
	 * update() method is used to update amount mismatch records.
	 * updateBatch() method is used to perform status mismatch records in the refund table.
	 */
	
	public long insert() {
		return jdbcTemplate.update(INSERT_REFUND_DETAILS);
	}
	
	
	public long update(RefundDetails refundDetails, RefundStatus refundStatus) {
		return jdbcTemplate.update(
			UPDATE_AMOUNT_MISMATCH,
			refundDetails.amount(),
			refundStatus.toString(),
			Timestamp.valueOf(LocalDateTime.now()),
			ProcessedStatus.TRUE.toString(),
			refundDetails.txnId()
		);
	}
	
	public long updateBatch(RefundStatus status) {
		return jdbcTemplate.update(
			UPDATE_STATUS_MISMATCH,
			status.toString(),
			Timestamp.valueOf(LocalDateTime.now()),
			ProcessedStatus.TRUE.toString()
		);
	}

}
