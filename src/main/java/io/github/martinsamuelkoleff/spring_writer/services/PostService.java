package io.github.martinsamuelkoleff.spring_writer.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.github.martinsamuelkoleff.spring_writer.dtos.PostDTO;
import io.github.martinsamuelkoleff.spring_writer.dtos.PostMdDTO;
import io.github.martinsamuelkoleff.spring_writer.dtos.PostRequestDTO;
import io.github.martinsamuelkoleff.spring_writer.entities.Post;
import io.github.martinsamuelkoleff.spring_writer.entities.enums.PostStatus;
import io.github.martinsamuelkoleff.spring_writer.repositories.PostRepository;
import io.github.martinsamuelkoleff.spring_writer.utils.SlugUtils;

@Service
public class PostService {

	private final CategoryService categoryService;
	private final TagService tagService;
	private final PostRepository postRepository;

	public PostService(PostRepository postRepository, CategoryService categoryService, TagService tagService) {
		this.postRepository = postRepository;
		this.categoryService = categoryService;
		this.tagService = tagService;
	}

	public Page<PostDTO> getPosts(Pageable pageable) {
		return postRepository.findAll(pageable).map(PostDTO::from);
	}

	public Page<PostDTO> getPublishedPosts(Pageable pageable) {
		return postRepository.findByStatus(PostStatus.PUBLISHED, pageable).map(PostDTO::from);
	}

	public Page<PostDTO> getPublishedPostsByTag(UUID tagId, Pageable pageable) {
		return postRepository.findByStatusAndTagsId(PostStatus.PUBLISHED, tagId, pageable).map(PostDTO::from);
	}

	public Page<PostDTO> getPublishedPostsByCategory(UUID categoryId, Pageable pageable) {
		return postRepository.findByStatusAndCategoryId(PostStatus.PUBLISHED, categoryId, pageable).map(PostDTO::from);
	}

	public Optional<PostMdDTO> getPostBySlug(String slug) {
		return postRepository.findBySlug(slug).map(PostMdDTO::from);
	}

	public Optional<PostMdDTO> getPostById(UUID id) {
		return postRepository.findById(id).map(PostMdDTO::from);
	}

	public void changePostStatus(UUID id, PostStatus status) {
		Post existingPost = postRepository.findById(id).orElseThrow(() -> {
			throw new IllegalArgumentException("Id no valido");
		});
		existingPost.setStatus(status);
		postRepository.save(existingPost);
	}

	public void create(PostRequestDTO request) {
		Post newPost = new Post();

		categoryService.findById(request.categoryId()).ifPresentOrElse(cat -> {
			newPost.setCategory(cat);
		}, () -> {
			throw new IllegalArgumentException("Categoria no valida");
		});

		request.tagIds().stream().forEach(tagId -> {
			tagService.findById(tagId).ifPresent(tag -> {
				newPost.getTags().add(tag);
			});
		});

		newPost.setTitle(request.title());
		newPost.setSlug(SlugUtils.toSlug(request.title()));
		newPost.setContentMd(request.contentMd());
		newPost.setExcerpt(request.excerpt());
		newPost.setStatus(request.status());

		if (request.status().equals(PostStatus.PUBLISHED)) {
			newPost.setPublishedAt(LocalDateTime.now());
		}

		if (request.status().equals(PostStatus.DRAFT)) {
			newPost.setPublishedAt(null);
		}

		postRepository.save(newPost);
	}

	public void update(UUID id, PostRequestDTO request) {
		Post existingPost = postRepository.findById(id).orElseThrow(() -> {
			throw new IllegalArgumentException("Id no valido");
		});

		categoryService.findById(request.categoryId()).ifPresentOrElse(existingPost::setCategory, () -> {
			throw new IllegalArgumentException("Categoria no valida");
		});

		existingPost.getTags().clear();
		request.tagIds().stream().forEach(tagId -> {
			tagService.findById(tagId).ifPresent(tag -> {
				existingPost.getTags().add(tag);
			});
		});

		existingPost.setTitle(request.title());
		existingPost.setSlug(SlugUtils.toSlug(request.title()));
		existingPost.setContentMd(request.contentMd());
		existingPost.setExcerpt(request.excerpt());
		existingPost.setStatus(request.status());

		if (request.status().equals(PostStatus.PUBLISHED)) {
			// si se publica...

			existingPost.setStatus(PostStatus.PUBLISHED); // cambiar estado a publicado

			if (existingPost.getPublishedAt() == null) {
				// si se publica por primera vez

				existingPost.setPublishedAt(LocalDateTime.now()); // setear fecha de publicacion
			} else {
				// ya se ha publicado, se mantiene la fecha de publicacion
			}
		} else {
			// si no se publica o se "des"publica...
			existingPost.setStatus(PostStatus.DRAFT); // cambiar estado a borrador
			//fecha de publicacion queda null o congelada
		}

		postRepository.save(existingPost);
	}

	public void delete(UUID id) {
		if (!postRepository.existsById(id)) {
			throw new IllegalArgumentException("Id no valido");
		}
		postRepository.deleteById(id);
	}

}
