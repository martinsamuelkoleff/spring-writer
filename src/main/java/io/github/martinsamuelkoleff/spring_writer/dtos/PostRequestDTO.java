package io.github.martinsamuelkoleff.spring_writer.dtos;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import io.github.martinsamuelkoleff.spring_writer.entities.Post;
import io.github.martinsamuelkoleff.spring_writer.entities.Tag;
import io.github.martinsamuelkoleff.spring_writer.entities.enums.PostStatus;

public record PostRequestDTO(
	    String title,
	    String contentMd,
	    String excerpt,
	    UUID categoryId,
	    PostStatus status,
	    Set<UUID> tagIds
	) {
	    public static PostRequestDTO from(Post post) {
	        return new PostRequestDTO(
	            post.getTitle(),
	            post.getContentMd(),
	            post.getExcerpt(),
	            post.getCategory() != null ?
	            		post.getCategory().getId() : null,
	            post.getStatus(),
	            post.getTags() != null ? 
	            		post.getTags().stream().map(Tag::getId).collect(Collectors.toSet()) : Set.of()
	        );
	    }
	}