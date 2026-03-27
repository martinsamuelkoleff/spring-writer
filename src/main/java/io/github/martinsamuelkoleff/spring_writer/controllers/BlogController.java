package io.github.martinsamuelkoleff.spring_writer.controllers;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import io.github.martinsamuelkoleff.spring_writer.dtos.CategoryDTO;
import io.github.martinsamuelkoleff.spring_writer.dtos.PostMdDTO;
import io.github.martinsamuelkoleff.spring_writer.dtos.TagDTO;
import io.github.martinsamuelkoleff.spring_writer.services.CategoryService;
import io.github.martinsamuelkoleff.spring_writer.services.CommentService;
import io.github.martinsamuelkoleff.spring_writer.services.PostService;
import io.github.martinsamuelkoleff.spring_writer.services.TagService;

@Controller
public class BlogController {

	private final PostService postService;
	private final CommentService commentService;
	private final CategoryService categoryService;
	private final TagService tagService;
	private final int CANTIDAD_POSTS = 10;
	
	public BlogController(
			PostService postService, 
			CommentService commentService, 
			CategoryService categoryService,
			TagService tagService
			) {
		this.postService = postService;
		this.commentService = commentService;
		this.categoryService = categoryService;
		this.tagService = tagService;
	}
	
	@GetMapping("/blog")
	public String getBlog(Model model, @RequestParam(defaultValue = "0") int page) {
		model.addAttribute("posts", postService.getPublishedPosts(
				PageRequest.of(page, CANTIDAD_POSTS,Sort.by(Direction.DESC, "publishedAt")))
				);
		model.addAttribute("categories", categoryService.getCategories());
	    model.addAttribute("tags", tagService.getTags());
		return "/blog/list";
	}
	
	@GetMapping("/blog/category/{slug}")
	public String getBlogByCategory(@PathVariable String slug, Model model,
	                                @RequestParam(defaultValue = "0") int page) {
	    CategoryDTO category = categoryService.getBySlug(slug)
	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

	    model.addAttribute("posts", postService.getPublishedPostsByCategory(
	        category.id(), PageRequest.of(page, CANTIDAD_POSTS, Sort.by(Direction.DESC, "publishedAt"))
	    ));
	    model.addAttribute("filtroActivo", "Categoría: " + category.name());
	    model.addAttribute("paginadorBase", "/blog/category/" + slug);
	    model.addAttribute("categories", categoryService.getCategories());
	    model.addAttribute("tags", tagService.getTags());
	    return "/blog/list";
	}

	@GetMapping("/blog/tag/{slug}")
	public String getBlogByTag(@PathVariable String slug, Model model,
	                           @RequestParam(defaultValue = "0") int page) {
	    TagDTO tag = tagService.getBySlug(slug)
	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

	    model.addAttribute("posts", postService.getPublishedPostsByTag(
	        tag.id(), PageRequest.of(page, CANTIDAD_POSTS, Sort.by(Direction.DESC, "publishedAt"))
	    ));
	    model.addAttribute("filtroActivo", "Tag: " + tag.name());
	    model.addAttribute("paginadorBase", "/blog/tag/" + slug);
	    model.addAttribute("categories", categoryService.getCategories());
	    model.addAttribute("tags", tagService.getTags());
	    return "/blog/list";
	}
	
	@GetMapping("/blog/{slug}")
	public String getPost(@PathVariable String slug,Model model, @RequestParam(defaultValue = "0") int page) {
		Optional<PostMdDTO> post = postService.getPostBySlug(slug);
		
	    if (post.isEmpty()) {
	        return "redirect:/blog";
	    }

	    model.addAttribute("post", post.get());
	    model.addAttribute("comments", commentService.getApprovedCommentsByPost(post.get().id(), 
	    		PageRequest.of(page, 5,Sort.by(Direction.DESC, "createdAt")))
	    		);
	    model.addAttribute("markdownContent", post.get().contentMd());

	    return "/blog/post";
	}
	
}
