package io.github.martinsamuelkoleff.spring_writer.dtos;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import io.github.martinsamuelkoleff.spring_writer.entities.Post;
import io.github.martinsamuelkoleff.spring_writer.entities.enums.PostStatus;

public record PostDTO(
    UUID id,
    String title,
    String slug,
    String excerpt,
    PostStatus status,
    LocalDateTime publishedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    CategoryDTO category,
    Set<TagDTO> tags
) {
    public static PostDTO from(Post post) {
        return new PostDTO(
            post.getId(),
            post.getTitle(),
            post.getSlug(),
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
