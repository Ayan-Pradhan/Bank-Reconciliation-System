package com.spring.projects.app.dto;

import java.time.LocalDateTime;

import com.spring.projects.app.constant.ResponseStatusCode;

public record ErrorResponse(
		ResponseStatusCode status,
		String message,
		LocalDateTime timeStamp
		) {}
