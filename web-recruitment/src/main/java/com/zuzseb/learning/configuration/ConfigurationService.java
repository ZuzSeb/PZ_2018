package com.zuzseb.learning.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

	// inject via application.properties
	@Value("${welcome.message:test}")
	private String message;

	@Value("${info.user.success.creation}")
	private String userSuccessfullyCreatedText;
	
	public String getMessage() {
		return message;
	}

	public String getUserSuccessfullyCreatedText() {
		return userSuccessfullyCreatedText;
	}
}
