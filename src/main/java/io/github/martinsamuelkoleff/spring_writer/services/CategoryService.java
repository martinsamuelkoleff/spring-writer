package io.github.martinsamuelkoleff.spring_writer.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.martinsamuelkoleff.spring_writer.dtos.CategoryDTO;
import io.github.martinsamuelkoleff.spring_writer.entities.Category;
import io.github.martinsamuelkoleff.spring_writer.repositories.CategoryRepository;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;
	
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	public List<CategoryDTO> getCategories(){
		return categoryRepository.findAll().stream().map(CategoryDTO::from).toList();
	}

	public Optional<CategoryDTO> getBySlug(String slug) {
		return categoryRepository.findBySlug(slug).map(CategoryDTO::from);
	}

	public Optional<Category> findById(UUID id) {
		return categoryRepository.findById(id);
	}
	
}
