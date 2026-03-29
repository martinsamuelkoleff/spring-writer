package io.github.martinsamuelkoleff.spring_writer.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.github.martinsamuelkoleff.spring_writer.services.PostService;

import org.springframework.ui.Model;

@Controller
public class HomeController {

	private final PostService postService;
	
	public HomeController(PostService postService) {
		this.postService = postService;
	}
	
	@GetMapping("/")
	public String getRoot(Model model) {
		model.addAttribute("posts", postService.getPublishedPosts(
				PageRequest.of(0, 4,Sort.by(Direction.DESC, "publishedAt")))
				);
		
		model.addAttribute("pageTitle", "Home");
		model.addAttribute("pageDescription", "Blog sobre desarrollo backend con Java y Spring Boot.");
	    model.addAttribute("currentUrl", "https://springwriter.com/");
	    
		return "home";
	}
	
}
