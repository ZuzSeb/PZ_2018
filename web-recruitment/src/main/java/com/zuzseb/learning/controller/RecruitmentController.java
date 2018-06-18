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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
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
    public String createPost(@ModelAttribute("post") Post post, HttpServletRequest request, HttpSession session, Map<String, Object> model) {
//        LOGGER.info("POST /users, User: {}", user);
        recruitmentRepository.save(post);
        //TODO npc
//        User user = userService.findByLogin(session.getAttribute("userName").toString());
//        Set<Post> posts = user.getPosts();
//        posts.add(post);
//        userService.update(user);
        return "recruitment";
    }

    @GetMapping("/add-post")
    public String getPost(@ModelAttribute("post") Post post, HttpServletRequest request, HttpSession session, Map<String, Object> model) {
//        LOGGER.info("POST /users, User: {}", user);
        session.invalidate();
        HttpSession newSession = request.getSession();
        return "add-post";
    }
}
