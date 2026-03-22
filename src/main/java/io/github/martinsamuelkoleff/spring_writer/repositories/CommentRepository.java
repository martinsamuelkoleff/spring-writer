package io.github.martinsamuelkoleff.spring_writer.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.martinsamuelkoleff.spring_writer.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, UUID>{

}
