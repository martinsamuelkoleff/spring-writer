package io.github.martinsamuelkoleff.spring_writer.controllers;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import io.github.martinsamuelkoleff.spring_writer.dtos.PostRequestDTO;
import io.github.martinsamuelkoleff.spring_writer.services.CategoryService;
import io.github.martinsamuelkoleff.spring_writer.services.CommentService;
import io.github.martinsamuelkoleff.spring_writer.services.PostService;
import io.github.martinsamuelkoleff.spring_writer.services.TagService;

@Controller
public class AdminController {
	
    private final PostService postService;
    private final CommentService commentService;
    private final CategoryService categoryService;
    private final TagService tagService;

    public AdminController(PostService postService, CommentService commentService,
                           CategoryService categoryService, TagService tagService) {
        this.postService = postService;
        this.commentService = commentService;
        this.categoryService = categoryService;
        this.tagService = tagService;
    }
	
    @GetMapping("/admin")
    public String getRoot() {
        return "admin/dashboard";
    }

    
    @GetMapping("/admin/posts")
    public String getPosts(Model model, @RequestParam(defaultValue = "0") int page) {
        model.addAttribute("posts", postService.getPosts(
            PageRequest.of(page, 15, Sort.by(Direction.DESC, "createdAt"))
        ));
        return "admin/posts";
    }

    @GetMapping("/admin/posts/new")
    public String getNewPostForm(Model model) {
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("tags",       tagService.getTags());
        return "admin/post-form";
    }

    @PostMapping("/admin/posts/new")
    public String createPost(@ModelAttribute PostRequestDTO request) {
        postService.create(request);
        return "redirect:/admin/posts";
    }

	@GetMapping("/admin/posts/{id}/edit")
	public String getEditPostForm(@PathVariable UUID id, Model model) {
		model.addAttribute("post",
				postService.getPostById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
		model.addAttribute("categories", categoryService.getCategories());
		model.addAttribute("tags", tagService.getTags());
		return "admin/post-form";
	}

	@PostMapping("/admin/posts/{id}/edit")
	public String updatePost(@PathVariable UUID id, @ModelAttribute PostRequestDTO request) {
		postService.update(id, request);
		return "redirect:/admin/posts";
	}

	@PostMapping("/admin/posts/{id}/delete")
	public String deletePost(@PathVariable UUID id) {
		postService.delete(id);
		return "redirect:/admin/posts";
	}
	
	@GetMapping("/admin/posts/{id}/comments")
    public String getComments(@PathVariable UUID id, Model model,
                              @RequestParam(defaultValue = "0") int page) {
        model.addAttribute("comments", commentService.getCommentsByPost(id,
            PageRequest.of(page, 20, Sort.by(Direction.DESC, "createdAt"))
        ));
        model.addAttribute("postId", id);
        return "admin/comments";
    }

    @PostMapping("/admin/posts/{postId}/comments/{commentId}/approve")
    public String approveComment(@PathVariable UUID postId,
                                 @PathVariable UUID commentId) {
        commentService.approve(commentId);
        return "redirect:/admin/posts/" + postId + "/comments";
    }

    @PostMapping("/admin/posts/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable UUID postId,
                                @PathVariable UUID commentId) {
        commentService.delete(commentId);
        return "redirect:/admin/posts/" + postId + "/comments";
    }
}
