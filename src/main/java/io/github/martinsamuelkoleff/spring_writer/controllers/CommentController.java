package io.github.martinsamuelkoleff.spring_writer.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import io.github.martinsamuelkoleff.spring_writer.dtos.CommentRequestDTO;
import io.github.martinsamuelkoleff.spring_writer.services.CommentService;

@Controller
public class CommentController {
	
	private final CommentService commentService;
	
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}
	
	@PostMapping("/blog/{slug}/comments")
	public String postComment(@PathVariable String slug,
	                          @ModelAttribute CommentRequestDTO request) {
	    commentService.savePendingComment(slug, request);
	    return "redirect:/blog/" + slug;
	}
}
