package com.zuzseb.learning.controller;

import com.zuzseb.learning.model.File;
import com.zuzseb.learning.model.PagerModel;
import com.zuzseb.learning.model.Post;
import com.zuzseb.learning.repository.PostRepository;
import com.zuzseb.learning.service.FileUploadService;
import com.zuzseb.learning.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class PostController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostController.class);

    private static final int BUTTONS_TO_SHOW = 3;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 5;
    private static final int[] PAGE_SIZES = {5, 10, 20};

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping("/all-posts")
    public String showAllPosts(@RequestParam("pageSize") Optional<Integer> pageSize,
                               @RequestParam("page") Optional<Integer> page,
                               Map<String, Object> model) {
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
        Page<Post> postsList = postRepository.findAll(new PageRequest(evalPage, evalPageSize));
        postsList.forEach(post -> post.setDescription(StringUtils.cut(post.getDescription(), 300)));
        PagerModel pager = new PagerModel(postsList.getTotalPages(), postsList.getNumber(), BUTTONS_TO_SHOW);

        model.put("posts", postsList);
        model.put("selectedPageSize", evalPageSize);
        model.put("pageSizes", PAGE_SIZES);
        model.put("pager", pager);
        return "all-posts";
    }

    @GetMapping("/posts/{postId}")
    public String getPost(@PathVariable("postId") Long postId, Map<String, Object> model) {
        LOGGER.info("/posts/{}", postId);
        Post post = postRepository.findOne(postId);
        model.put("post", post);
        return "post";
    }

    @GetMapping("recruit-post/{postId}")
    public String getRecruitPosts(@PathVariable("postId") Long postId, Map<String, Object> model) {
        LOGGER.info("/post/{}", postId);
        Post post = postRepository.findOne(postId);
        List<File> files = fileUploadService.findByPost(post);
        model.put("post", post);
        model.put("files", files);
        return "recruit-post";
    }

    @PostMapping("/delete-post/{postId}")
    public String deletePost(@PathVariable("postId") Long postId,Map<String, Object> model) {
        postRepository.delete(postId);
        model.put("infoMessage", "Post successfully deleted.");
        return "info/success";
    }

}
