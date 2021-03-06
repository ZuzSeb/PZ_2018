package com.zuzseb.learning.controller;

import com.zuzseb.learning.exception.ComparisonPasswordException;
import com.zuzseb.learning.exception.UserNotFoundException;
import com.zuzseb.learning.exception.WrongActualPasswordException;
import com.zuzseb.learning.model.*;
import com.zuzseb.learning.repository.PostRepository;
import com.zuzseb.learning.service.FileUploadService;
import com.zuzseb.learning.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final FileUploadService fileUploadService;
    private final PostRepository postRepository;

    @Autowired
    public UserController(FileUploadService fileUploadService, UserService userService, PostRepository postRepository) {
        this.fileUploadService = fileUploadService;
        this.userService = userService;
        this.postRepository = postRepository;
    }

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
            newSession.setAttribute("role", user.getRole());
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

    @GetMapping("/users/{login}/posts/{post-id}")
    public String deleteUserPost(@PathVariable("login") String login, @PathVariable("post-id") Long postId, Map<String, Object> model) {
        LOGGER.info("DELETE /users/{}/posts/{}", login, postId);
        try {
            User foundUser = userService.findByLogin(login);
            Post foundPost = postRepository.findOne(postId);
            fileUploadService.deleteFileByUserAndPost(foundUser, foundPost);
        } catch (Exception e) {
            LOGGER.warn("" + e);
            model.put("infoMessage", "We're sorry. Something went wrong.");
            return "info/error";
        }
        model.put("infoMessage", "Post deleted.");
        return "info/success";
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
    public String getProfile(@PathVariable("login") String login, Map<String, Object> model) throws UserNotFoundException {
        User foundUser = userService.findByLogin(login);
        List<File> files = fileUploadService.findByUser(foundUser);
        List<Post> posts = files.stream().map(File::getPost).collect(Collectors.toList());
        model.put("user", foundUser);
        model.put("posts", posts);
        return "profile";
    }

    @PostMapping("/login")
    public String login(Login login, HttpServletRequest request, HttpSession session) {
        session.invalidate();
        HttpSession newSession;
        User user;
        try {
            user = userService.findByLogin(login.getLogin());
        } catch (UserNotFoundException e) {
            return "no-access";
        }
        if (userService.authenticate(login)) {
            newSession = request.getSession();
            newSession.setAttribute("username", login.getLogin());
            newSession.setAttribute("role", user.getRole());
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
