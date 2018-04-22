package com.zuzseb.learning.controller;

import com.zuzseb.learning.configuration.ConfigurationService;
import com.zuzseb.learning.model.User;
import com.zuzseb.learning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class UserController {

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/user")
    public String createUser(@ModelAttribute("user") User user, Map<String, Object> model) {
        System.out.println("User: " + user);
        userRepository.save(user);
        model.put("infoMessage", configurationService.getUserSuccessfullyCreatedText());
        return "info/success";
    }

    @GetMapping("log-in")
    public String logIn() {
        return "/";
    }
}
