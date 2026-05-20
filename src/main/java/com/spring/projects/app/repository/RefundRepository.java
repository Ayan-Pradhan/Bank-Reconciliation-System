package com.spring.projects.app.repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.spring.projects.app.constant.RefundStatus;
import com.spring.projects.app.dto.RefundDetails;

@Repository
public class RefundRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate; 
	
	private static final String INSERT_INTO_REFUND_STATUS_MISMATCH = """
				INSERT INTO refund (txn_id, amount)
				SELECT txn_id, bank_amount FROM report r
				WHERE r.discrepency_type = 'STATUS_MISMATCH';
			""";
	
	private static final String UPDATE_REFUND_STATUS_MISMATCH = """
			UPDATE refund SET refund_status = ?, time_stamp = ?, is_processed = ?
			WHERE is_processed = 'FALSE'
			""";
	
	private static final String UPDATE_REFUND_AMOUNT_MISMATCH = """
			UPDATE refund SET refund_status = ?, time_stamp = ?, is_processed = ?
			WHERE txn_id = ? AND is_processed = 'FALSE'
			""";
	
	public long insert() {
		return jdbcTemplate.update(INSERT_INTO_REFUND_STATUS_MISMATCH);
	}
	
	public long update(RefundDetails refundDetails) {
		return jdbcTemplate.update(connection->{
			PreparedStatement ps = connection.prepareStatement(UPDATE_REFUND_AMOUNT_MISMATCH);
			ps.setString(1, refundDetails.refundStatus());
			ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
			ps.setString(3, refundDetails.txnId());
			ps.setString(3, "TRUE");
			return ps;
		});
	}
	
	public long updateBatch(RefundStatus status) {
		return jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(UPDATE_REFUND_STATUS_MISMATCH);
			ps.setString(1, status.toString());
			ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
			ps.setString(3, "TRUE");
			return ps;
		});
	}

}
