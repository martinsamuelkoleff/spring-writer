package io.github.martinsamuelkoleff.spring_writer.dtos;

import java.time.LocalDateTime;
import java.util.UUID;
import io.github.martinsamuelkoleff.spring_writer.entities.Comment;
import io.github.martinsamuelkoleff.spring_writer.entities.enums.CommentStatus;

public record CommentDTO(
    UUID id,
    String authorName,
    String body,
    CommentStatus status,
    LocalDateTime createdAt,
    String authorEmail,
    UUID postId,
    String postSlug
) {
    public static CommentDTO from(Comment comment) {
        return new CommentDTO(
            comment.getId(),
            comment.getAuthorName(),
            comment.getBody(),
            comment.getStatus(),
            comment.getCreatedAt(),
            comment.getAuthorEmail(),
            comment.getPost().getId(),
            comment.getPost().getSlug()
        );
    }
}
