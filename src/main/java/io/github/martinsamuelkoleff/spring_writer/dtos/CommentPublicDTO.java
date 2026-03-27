package io.github.martinsamuelkoleff.spring_writer.dtos;

import java.time.LocalDateTime;

import io.github.martinsamuelkoleff.spring_writer.entities.Comment;

public record CommentPublicDTO(
	    String authorName,
	    String body,
	    LocalDateTime createdAt
	    ) {
	    public static CommentPublicDTO from(Comment comment) {
	        return new CommentPublicDTO(
	            comment.getAuthorName(),
	            comment.getBody(),
	            comment.getCreatedAt()
	            );
	    }
	}
