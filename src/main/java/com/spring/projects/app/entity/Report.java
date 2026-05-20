package com.spring.projects.app.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Report {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String txnId;
	private String description;
	private Double bankAmount;
	private Double userAmount;
	private String discrepencyType;
	private String bankPaymentStatus;
	private String userPaymentStatus;
	private String refundStatus;
	private LocalDateTime timeStamp;
	
	@ColumnDefault(value = "'FALSE'")
	private String isProcessed;

}
