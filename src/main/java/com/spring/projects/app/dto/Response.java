package com.spring.projects.app.dto;

import com.spring.projects.app.constant.ResponseStatusCode;

public record Response(
		ResponseStatusCode status,
		String message
		) {}
