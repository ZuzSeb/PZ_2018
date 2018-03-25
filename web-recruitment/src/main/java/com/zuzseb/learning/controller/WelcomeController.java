package com.zuzseb.learning.controller;

import java.util.Arrays;
import java.util.Map;

import com.zuzseb.learning.configuration.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zuzseb.learning.service.LogIn;

@Controller
public class WelcomeController {
	
	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private LogIn logIn;
	
	@RequestMapping("/")
	public String logIn(Map<String, Object> model) {
		model.put("message", configurationService.getMessage());
		model.put("cat", "Cat");
		return "log-in";
	}
	
	@RequestMapping("/welcome")
	public String welcome() {
		return "welcome";
	}
	
	@RequestMapping("/about")
	public String about(Map<String, Object> model) {
		model.put("message", configurationService.getMessage());
		model.put("cat", "Cat");
		return "about";
	}
	
	@RequestMapping("/log-out")
	public String logInPage() {
		return "log-in";
	}
	
	@RequestMapping(value = "/log-in", method = RequestMethod.GET)
	public String logIn(@RequestParam("email") String email, @RequestParam("pwd") String pwd, Map<String, Object> model) {
		model.put("message", Arrays.asList(email.split("@")).get(0));
		return (logIn.checkCredencials(email, pwd)) ? "welcome" : "no-access";
	}
	
	@RequestMapping("/no-access")
	public String noAccess() {
		return "no-access";
	}
	
	@RequestMapping("/adding-pet")
	public String addingPetPage() {
		return "adding-pet";
	}
	
	@RequestMapping("/listing-all-pets")
	public String listigAllPetsPage() {
		return "listing-all-pets";
	}
	
}