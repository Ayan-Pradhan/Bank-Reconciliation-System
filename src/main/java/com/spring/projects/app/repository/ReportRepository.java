package com.spring.projects.app.repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.spring.projects.app.constant.DiscrepencyType;
import com.spring.projects.app.constant.RefundStatus;
import com.spring.projects.app.entity.Report;

@Repository
public class ReportRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String INSERT_INTO_REPORT_AMOUNT_MISMATCHED = """
			INSERT INTO report (txn_id, bank_amount, user_amount, bank_payment_status, user_payment_status)
			SELECT a.txn_id, a.amount, b.amount, a.status, b.status
			FROM staging_bank_records a LEFT JOIN staging_user_records b ON a.txn_id = b.txn_id
			WHERE a.amount <> b.amount
			""";
	
	private static final String UPDATE_REPORT_AMOUNT_MISMATCHED = """
			UPDATE report SET description = ?, discrepency_type = ?, refund_status = ?, time_stamp = ?, is_processed = ?
			WHERE bank_amount <> user_amount AND is_processed = 'FALSE'			
			""";
	
	private static final String INSERT_INTO_REPORT_STATUS_MISMATCHED = """
			INSERT INTO report (txn_id, bank_amount, user_amount, bank_payment_status, user_payment_status)
			SELECT a.txn_id, a.amount, b.amount, a.status, b.status
			FROM staging_bank_records a LEFT JOIN staging_user_records b ON a.txn_id = b.txn_id
			WHERE a.status <> b.status
			""";
	
	// updates refund status to INITIATED
	private static final String UPDATE_REPORT_STATUS_MISMATCHED = """
			UPDATE report SET description = ?, discrepency_type = ?, refund_status = ?, time_stamp = ?, is_processed = ?
			WHERE bank_payment_status <> user_payment_status AND is_processed = 'FALSE'
			""";
	
	// updates refund status for INITIATED batch refunds
	private static final String UPDATE_REPORT_INITIATED = """
			UPDATE report SET refund_status = ?
			WHERE refund_status = 'INITIATED'
			""";
	
	// updates refund status for NO_ACTION refunds
	private static final String UPDATE_REPORT_NO_ACTION = """
			UPDATE report SET refund_status = ?
			WHERE refund_status = 'NO_ACTION' AND txn_id = ?
			""";
	
	public long insertAmountMismatch() {
		return jdbcTemplate.update(INSERT_INTO_REPORT_AMOUNT_MISMATCHED);
	}
	
	public void updateReportForAmountMismatch(String description, DiscrepencyType type, RefundStatus status) {
		jdbcTemplate.update(connection->{
			PreparedStatement ps = connection.prepareStatement(UPDATE_REPORT_AMOUNT_MISMATCHED);
			ps.setString(1, description);
			ps.setString(2, type.toString());
			ps.setString(3, status.toString());
			ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
			ps.setString(5, "TRUE");
			return ps;
		});

	}
	
	public long insertStatusMismatch() {
		return jdbcTemplate.update(INSERT_INTO_REPORT_STATUS_MISMATCHED);
	}
	
	public void updateReportForStatusMismatch(String description, DiscrepencyType type, RefundStatus status) {
		jdbcTemplate.update(connection->{
			PreparedStatement ps = connection.prepareStatement(UPDATE_REPORT_STATUS_MISMATCHED);
			ps.setString(1, description);
			ps.setString(2, type.toString());
			ps.setString(3, status.toString());
			ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
			ps.setString(5, "TRUE");
			return ps;
		});
	}
	
	public void updateReportForInitiatedRefund(RefundStatus status) {
		jdbcTemplate.update(connection->{
			PreparedStatement ps = connection.prepareStatement(UPDATE_REPORT_INITIATED);
			ps.setString(1, status.toString());
			return ps;
		});
	}
	
	public void updateReportForNoActionRefund(Report report) {
		jdbcTemplate.update(connection->{
			PreparedStatement ps = connection.prepareStatement(UPDATE_REPORT_NO_ACTION);
			ps.setString(1, report.getRefundStatus());
			ps.setString(2, report.getTxnId());
			return ps;
		});
	}
}
