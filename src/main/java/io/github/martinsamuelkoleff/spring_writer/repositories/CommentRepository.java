package io.github.martinsamuelkoleff.spring_writer.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import io.github.martinsamuelkoleff.spring_writer.entities.Comment;
import io.github.martinsamuelkoleff.spring_writer.entities.enums.CommentStatus;

public interface CommentRepository extends JpaRepository<Comment, UUID>{
	
	Page<Comment> findByStatusAndPostId(CommentStatus status, UUID postId,Pageable pageable);

	Page<Comment> findAllByPostId(UUID id, Pageable pageable);
	
	Page<Comment> findAllByStatus(CommentStatus status, Pageable pageable);
	
}
