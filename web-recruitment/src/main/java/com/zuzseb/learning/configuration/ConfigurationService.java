package com.zuzseb.learning.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

	// inject via application.properties
	@Value("${welcome.message:test}")
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
