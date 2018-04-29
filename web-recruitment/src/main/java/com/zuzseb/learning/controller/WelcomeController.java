package com.zuzseb.learning.controller;

import com.zuzseb.learning.configuration.ConfigurationService;
import com.zuzseb.learning.model.PagerModel;
import com.zuzseb.learning.model.Post;
import com.zuzseb.learning.repository.PostRepository;
import com.zuzseb.learning.service.LogIn;
import com.zuzseb.learning.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Controller
public class WelcomeController {

	private static final int BUTTONS_TO_SHOW = 3;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 5;
	private static final int[] PAGE_SIZES = { 5, 10, 20};

	
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
    public String showAllPosts(@RequestParam("pageSize") Optional<Integer> pageSize,
							   @RequestParam("page") Optional<Integer> page,
							   Map<String,Object> model) {
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
		Page<Post> postsList = postRepository.findAll(new PageRequest(evalPage, evalPageSize));
		postsList.forEach(post -> post.setDescription(StringUtils.cut(post.getDescription(),300)));
		PagerModel pager = new PagerModel(postsList.getTotalPages(), postsList.getNumber(), BUTTONS_TO_SHOW);

		model.put("posts", postsList);
		model.put("selectedPageSize", evalPageSize);
		model.put("pageSizes", PAGE_SIZES);
		model.put("pager", pager);
		return "all-posts";
    }

	@GetMapping("/post")
	public String getPost(@RequestParam("id") Long itemid, Map<String,Object> model){
		Post post = postRepository.findOne(itemid);
		model.put("post", post);
		return "post";
	}
}
