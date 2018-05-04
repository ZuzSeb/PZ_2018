package com.zuzseb.learning.controller;

import com.zuzseb.learning.exception.ComparisonPasswordException;
import com.zuzseb.learning.exception.UserNotFoundException;
import com.zuzseb.learning.exception.WrongActualPasswordException;
import com.zuzseb.learning.model.Login;
import com.zuzseb.learning.model.PwdChange;
import com.zuzseb.learning.model.User;
import com.zuzseb.learning.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

@Controller
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public String createUser(@ModelAttribute("user") User user, HttpServletRequest request, HttpSession session, Map<String, Object> model) {
        LOGGER.info("POST /users, User: {}", user);
        session.invalidate();
        HttpSession newSession = request.getSession();
        if (userService.isEmailTaken(user.getEmail())) {
            model.put("infoMessage", "User with this email already exists!!!");
            return "info/error";
        } else {
            userService.save(user);
            newSession.setAttribute("username", user.getLogin());
            model.put("infoMessage", "User successfully created.");
            return "info/success";
        }
    }

    @PatchMapping("/users")
    public String updateUser(@ModelAttribute("user") User user, Map<String, Object> model) {
        LOGGER.info("PATCH /users, User: {}", user);
        Optional<User> updatedUser = userService.update(user);
        if (updatedUser.isPresent()) {
            model.put("infoMessage", "User successfully updated.");
            return "info/success";
        } else {
            model.put("infoMessage", "We're sorry. Something went wrong.");
            return "info/error";
        }
    }

    @PatchMapping("/users/{login}/pwd")
    public String changePassword(@PathVariable("login") String login, PwdChange pwdChange, Map<String, Object> model) {
        LOGGER.info("PATCH /users/{}/pwd", login);
        try {
            userService.changePwd(login, pwdChange);
        } catch (WrongActualPasswordException e) {
            model.put("infoMessage", "Wrong actual password. Forgot password? Please contact administrator.");
            return "info/error";
        } catch (ComparisonPasswordException e) {
            model.put("infoMessage", "Retyped new password is not the same as original.");
            return "info/error";
        } catch (UserNotFoundException e) {
           model.put("infoMessage", "User fas not found.");
           return "info/error";
        }
        model.put("infoMessage", "Password successfully changed.");
        return "info/success";
    }

    @GetMapping("/profiles/{login}")
    public String getProfile(@PathVariable("login") String login, Map<String, Object> model) {
        User foundUser = userService.findByLogin(login);
        model.put("user", foundUser);
        model.put("posts", foundUser.getPosts());
        return "profile";
    }

    @PostMapping("/login")
    public String login(Login login, HttpServletRequest request, HttpSession session) {
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
    public String logout(HttpSession session) {
        session.invalidate();
        return "welcome";
    }
}
