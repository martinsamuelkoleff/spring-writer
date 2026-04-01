package io.github.martinsamuelkoleff.spring_writer.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.martinsamuelkoleff.spring_writer.dtos.CommentRequestDTO;
import io.github.martinsamuelkoleff.spring_writer.services.CommentService;

@Controller
public class CommentController {
	
	private final CommentService commentService;
	
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}
	
	@GetMapping("/admin/comments")
	public String getPendingComments(Model model, @RequestParam(defaultValue = "0") int page) {
		
		model.addAttribute("pageTitle", "Comentarios pendientes");
		
		model.addAttribute("comments", 
				commentService.getPendingComments( PageRequest.of(page, 20, Sort.by(Direction.DESC, "createdAt")))
				);
		
		return "admin/pending-comments";
	}
	
	@PostMapping("/blog/{slug}/comments")
	public String postComment(@PathVariable String slug,
	                          @ModelAttribute CommentRequestDTO request) {
	    commentService.savePendingComment(slug, request);
	    return "redirect:/blog/" + slug;
	}
}
