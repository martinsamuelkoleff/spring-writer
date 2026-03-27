package io.github.martinsamuelkoleff.spring_writer.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.martinsamuelkoleff.spring_writer.dtos.TagDTO;
import io.github.martinsamuelkoleff.spring_writer.entities.Tag;
import io.github.martinsamuelkoleff.spring_writer.repositories.TagRepository;

@Service
public class TagService {

	private final TagRepository tagRepository;
	
	public TagService(TagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}
	
	public List<TagDTO> getTags(){
		return tagRepository.findAll().stream().map(TagDTO::from).toList();
	}

	public Optional<TagDTO> getBySlug(String slug) {
		return tagRepository.findBySlug(slug).map(TagDTO::from);
	}
	
	public Optional<Tag> findById(UUID id) {
		return tagRepository.findById(id);
	}
	
}

