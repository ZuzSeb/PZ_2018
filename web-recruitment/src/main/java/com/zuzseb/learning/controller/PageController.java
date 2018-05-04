package com.zuzseb.learning.controller;

import com.zuzseb.learning.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * This controller handles displaying pages. No logic in requests.
 */
@Controller
public class PageController {

    @GetMapping("/")
    public String welcomePage() {
        return "welcome";
    }

    @GetMapping("/login-page")
    public String logIn() {
        return "login-page";
    }

    @GetMapping("/registration")
    public String registration(Map<String, Object> model) {
        model.put("user", new User());
        return "registration";
    }

    @GetMapping("/no-access")
    public String noAccess() {
        return "no-access";
    }


}
