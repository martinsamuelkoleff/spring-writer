package io.github.martinsamuelkoleff.spring_writer.dtos;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import io.github.martinsamuelkoleff.spring_writer.entities.Post;
import io.github.martinsamuelkoleff.spring_writer.entities.enums.PostStatus;

public record PostMdDTO(
	    UUID id,
	    String title,
	    String slug,
	    String contentMd,
	    String excerpt,
	    PostStatus status,
	    LocalDateTime publishedAt,
	    LocalDateTime createdAt,
	    LocalDateTime updatedAt,
	    CategoryDTO category,
	    Set<TagDTO> tags
	) {
	    public static PostMdDTO from(Post post) {
	        return new PostMdDTO(
	            post.getId(),
	            post.getTitle(),
	            post.getSlug(),
	            post.getContentMd(),
	            post.getExcerpt(),
	            post.getStatus(),
	            post.getPublishedAt(),
	            post.getCreatedAt(),
	            post.getUpdatedAt(),
	            post.getCategory() != null ? CategoryDTO.from(post.getCategory()) : null,
        		post.getTags().stream().map(TagDTO::from).collect(Collectors.toSet())
	        );
	    }
	}

