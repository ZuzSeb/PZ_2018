package com.zuzseb.learning.controller;

import com.zuzseb.learning.configuration.ConfigurationService;
import com.zuzseb.learning.model.Post;
import com.zuzseb.learning.repository.PostRepository;
import com.zuzseb.learning.service.LogIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class WelcomeController {
	
	@Autowired
	private ConfigurationService configurationService;

	@Autowired
    private PostRepository postRepository;

	@Autowired
	private LogIn logIn;
	

	@RequestMapping("/welcome")
	public String welcome() {
		return "welcome";
	}
	
	@RequestMapping("/about")
	public String about(Map<String, Object> model) {
		model.put("message", configurationService.getMessage());
		model.put("cat", "Cat");
		return "about";
	}
	
	@RequestMapping("/log-out")
	public String logInPage() {
		return "log-in";
	}
	
	@RequestMapping(value = "/authorization", method = RequestMethod.GET)
	public String logIn(@RequestParam("email") String email, @RequestParam("pwd") String pwd, Map<String, Object> model) {
		model.put("message", Arrays.asList(email.split("@")).get(0));
		return (logIn.checkCredencials(email, pwd)) ? "welcome" : "no-access";
	}
	
	@RequestMapping("/adding-pet")
	public String addingPetPage() {
		return "adding-pet";
	}
	
	@RequestMapping("/listing-all-pets")
	public String listigAllPetsPage() {
		return "listing-all-pets";
	}

	@GetMapping("/all-posts")
    public String showAllPosts(Map<String, Object> model) {
        List<Post> posts = postRepository.findAll();
        model.put("posts", posts);
	    return "all-posts";
    }

    @GetMapping("/post")
//	@RequestMapping(value="/post", method = RequestMethod.GET)
	public String getPost(@RequestParam("id") String itemid, Map<String,Object> model){
		Post post = postRepository.findOne(itemid);
		model.put("post", post);
		return "post";
	}
}
