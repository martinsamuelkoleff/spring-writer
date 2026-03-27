package io.github.martinsamuelkoleff.spring_writer.dtos;

public record CommentRequestDTO(
	    String authorName,
	    String authorEmail,
	    String body
	) {}
