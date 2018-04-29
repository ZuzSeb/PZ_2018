package com.zuzseb.learning.controller;

import com.zuzseb.learning.configuration.ConfigurationService;
import com.zuzseb.learning.model.Login;
import com.zuzseb.learning.model.User;
import com.zuzseb.learning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ConfigurationService configurationService;

    @PostMapping("/users")
    public String createUser(@ModelAttribute("user") User user, HttpServletRequest request, HttpSession session, Map<String, Object> model) {
        System.out.println("User: " + user);
        session.invalidate();
        HttpSession newSession = request.getSession();
        if (userService.isEmailTaken(user.getEmail())) {
            model.put("infoMessage", "User with this email already exists!!!");
            return "info/error";
        } else {
            userService.save(user);
            newSession.setAttribute("username", user.getLogin());
            model.put("infoMessage", configurationService.getUserSuccessfullyCreatedText());
            return "info/success";
        }
    }

    @GetMapping("/profiles/{login}")
    public String getProfile(@PathVariable("login") String login, Map<String, Object> model) {
        User foundUser = userService.findByLogin(login);
        model.put("user", foundUser);
        model.put("posts", foundUser.getPosts());
        return "profile";
    }

    @PostMapping("/login")
    public String login(Login login, HttpServletRequest request, HttpSession session, Map<String, Object> model) {
        session.invalidate();
        HttpSession newSession;
        if (userService.authenticate(login)) {
            newSession = request.getSession();
            newSession.setAttribute("username", login.getLogin());
            return "welcome";
        } else {
            return "no-access";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, Map<String, Object> model) {
        session.invalidate();
        return "welcome";
    }
}
