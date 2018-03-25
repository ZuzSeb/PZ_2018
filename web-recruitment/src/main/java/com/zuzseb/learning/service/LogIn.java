package com.zuzseb.learning.service;

import org.springframework.stereotype.Service;

@Service
public class LogIn {

	private String email = "aaa@aa.aa";
	private String pwd = "aaa";

	public boolean checkCredencials(String email, String pwd) {
		return email.equals(this.email) && pwd.equals(this.pwd);
	}

}
