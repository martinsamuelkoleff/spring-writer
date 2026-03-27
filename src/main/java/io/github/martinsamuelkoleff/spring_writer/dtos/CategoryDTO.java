package io.github.martinsamuelkoleff.spring_writer.dtos;

import java.util.UUID;
import io.github.martinsamuelkoleff.spring_writer.entities.Category;

public record CategoryDTO(UUID id, String name, String slug) {

    public static CategoryDTO from(Category category) {
        return new CategoryDTO(
            category.getId(),
            category.getName(),
            category.getSlug()
        );
    }
}