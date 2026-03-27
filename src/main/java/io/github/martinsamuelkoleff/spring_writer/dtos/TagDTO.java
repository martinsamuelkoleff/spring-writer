package io.github.martinsamuelkoleff.spring_writer.dtos;

import java.util.UUID;
import io.github.martinsamuelkoleff.spring_writer.entities.Tag;

public record TagDTO(UUID id, String name, String slug) {

    public static TagDTO from(Tag tag) {
        return new TagDTO(
            tag.getId(),
            tag.getName(),
            tag.getSlug()
        );
    }
}
