package com.zuzseb.learning.controller;

import com.zuzseb.learning.model.Post;
import com.zuzseb.learning.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class WelcomeController {
	@Autowired
    private PostRepository postRepository;

	@RequestMapping("/welcome")
	public String welcome() {
		return "welcome";
	}
	
	@GetMapping("/all-posts")
    public String showAllPosts(Map<String, Object> model) {
        List<Post> posts = postRepository.findAll();
        model.put("posts", posts);
	    return "all-posts";
    }

    @GetMapping("/post")
	public String getPost(@RequestParam("id") String itemid, Map<String,Object> model){
		Post post = postRepository.findOne(itemid);
		model.put("post", post);
		return "post";
	}
}
