package com.zuzseb.learning.controller;

import com.zuzseb.learning.exception.UserNotFoundException;
import com.zuzseb.learning.model.File;
import com.zuzseb.learning.model.Post;
import com.zuzseb.learning.model.User;
import com.zuzseb.learning.repository.FileUploadRepository;
import com.zuzseb.learning.repository.PostRepository;
import com.zuzseb.learning.service.FileUploadService;
import com.zuzseb.learning.service.UserService;
import com.zuzseb.learning.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Controller
public class FileUploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/upload/{postId}")
    public String upload(@PathVariable("postId") Long postId, Map<String, Object> model) {
        LOGGER.info("GET /upload/{}", postId);
        Post post = postRepository.findOne(postId);
        post.setDescription(post.getDescription().length() > 300 ? StringUtils.cut(post.getDescription(), 300) : post.getDescription());
        model.put("post", post);
        return "upload";
    }

    @PostMapping("/upload/{postId}")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   HttpSession session,
                                   @PathVariable("postId") Long postId,
                                   Map<String, Object> model) {
        LOGGER.info("POST /upload/{}", postId);
        if (file != null) {

                try {
                    byte[] bytes = file.getBytes();
                    String login = session.getAttribute("username").toString();
                    User user = userService.findByLogin(login);
                    Post post = postRepository.findOne(postId);
                    if (fileUploadService.applyForPodst(user, post)) {
                        File uploadFile = new File(file.getOriginalFilename(), bytes, user, postRepository.findOne(postId));
                        fileUploadService.save(uploadFile);
                    } else {
                        model.put("infoMessage", "You've applied for this post already. Please delete your submission and then reapply.");
                        return "info/error";
                    }
                } catch (IOException e) {
                    LOGGER.error("Uploading file error");
                    model.put("infoMessage", "Ups, something went wrong.");
                    return "info/error";
                } catch (UserNotFoundException e) {
                    model.put("infoMessage", "User was not found.");
                    return "info/error";
                }
                model.put("infoMessage", "Successfully applied.");
                return "info/success";
            } else {
                model.put("infoMessage", "File was not given.");
                return "info/error";
            }

    }
}