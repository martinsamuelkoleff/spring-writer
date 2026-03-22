package io.github.martinsamuelkoleff.spring_writer.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.martinsamuelkoleff.spring_writer.entities.Tag;

public interface TagRepository extends JpaRepository<Tag,UUID>{

}
