package com.zuzseb.learning.controller;

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
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Controller
public class FileUploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private FileUploadRepository fileUploadRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/upload/{postId}")
    public String upload(@PathVariable("postId") Long postId, Map<String,Object> model){
        LOGGER.info("/upload/{} GET", postId);
        Post post = postRepository.findOne(postId);
        post.setDescription(StringUtils.cut(post.getDescription(),300));
        model.put("post", post);
        return "upload";
    }

    @PostMapping("/upload/{postId}")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes,
                                   HttpServletRequest request, HttpSession session,
                                   @PathVariable("postId") Long postId) {
        LOGGER.info("/upload/{} POST", postId);
        session.invalidate();
        HttpSession httpSession = request.getSession();
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            //TODO zalogowany user
//            User user = userService.findByLogin(httpSession.getAttribute("userName").toString());
            File uploadFile = new File(file.getOriginalFilename(), bytes);
            uploadFile.setPost(postRepository.findOne(postId));
//            if(user != null){
//                uploadFile.setUser(user);
//            } else {
//                LOGGER.info("No logged user");
//            }
            fileUploadService.save(uploadFile);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return "all-posts";
    }

//    @RequestMapping(value = "/downloadFile/{fileId}", method = RequestMethod.GET)
//    @ResponseBody
//    public FileSystemResource downloadFile(@Param(value="fileId") Long id) {
//        File file = fileUploadRepository.findOne(id);
//        return new FileSystemResource(file.getFileData().);
//    }
}