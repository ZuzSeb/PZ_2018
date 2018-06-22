package com.zuzseb.learning.controller;

import com.zuzseb.learning.model.Post;
import com.zuzseb.learning.model.User;
import com.zuzseb.learning.repository.RecruitmentRepository;
import com.zuzseb.learning.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Set;

@Controller
public class RecruitmentController  {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecruitmentController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @GetMapping("/recruitment/{login}")
    public String getRecruitment(@PathVariable("login") String login,
                               Map<String, Object> model) {
        LOGGER.info("USER {}", login);
        User foundUser = userService.findByLogin(login);
        model.put("user", foundUser);
        model.put("posts", foundUser.getPosts());
        return "recruitment";
    }


    @PostMapping("/addPost")
    public String createPost(@ModelAttribute("post") Post post, HttpSession session, Map<String, Object> model) {
        if (post != null) {
            recruitmentRepository.save(post);
        }
        User user = userService.findByLogin(session.getAttribute("username").toString());
        Set<Post> posts = user.getPosts();
        posts.add(post);
        userService.update(user);
        model.put("infoMessage", "Post successfully added.");
        return "info/success";
    }

    @GetMapping("/add-post")
    public String getPost() {
        return "add-post";
    }
}
