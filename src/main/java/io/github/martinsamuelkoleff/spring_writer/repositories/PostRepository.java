package io.github.martinsamuelkoleff.spring_writer.repositories;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import io.github.martinsamuelkoleff.spring_writer.entities.Post;
import io.github.martinsamuelkoleff.spring_writer.entities.enums.PostStatus;


public interface PostRepository extends JpaRepository<Post, UUID>{

	Page<Post> findByStatus(PostStatus status, Pageable pageable);
	Optional<Post> findBySlug(String slug);
	Page<Post> findByStatusAndCategoryId(PostStatus status,UUID categoryId, Pageable pageable);
	Page<Post> findByStatusAndTagsId(PostStatus status,UUID tagId, Pageable pageable);
	
    List<Post> findByStatus(PostStatus status);


	
}
