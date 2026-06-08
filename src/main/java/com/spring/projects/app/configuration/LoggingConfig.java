package com.spring.projects.app.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {
	
	@Bean
	Logger logger() {
		return LoggerFactory.getLogger(getClass());
	}

}
