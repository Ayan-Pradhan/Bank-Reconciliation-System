package com.spring.projects.app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
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
public class UserRecord {
	
	@Id
	private Long id;
	
	private String txnId;
	private String sender;
	private String receiver;
	private Double amount;
	private LocalDateTime timeStamp;
	private String status;

}
