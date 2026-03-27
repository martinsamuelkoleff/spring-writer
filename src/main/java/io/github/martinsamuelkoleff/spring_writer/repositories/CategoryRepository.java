package io.github.martinsamuelkoleff.spring_writer.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.martinsamuelkoleff.spring_writer.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID>{
	Optional<Category> findBySlug(String slug);
}
