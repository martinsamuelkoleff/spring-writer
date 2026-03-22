package io.github.martinsamuelkoleff.spring_writer.jpa;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import io.github.martinsamuelkoleff.spring_writer.entities.Post;
import io.github.martinsamuelkoleff.spring_writer.entities.enums.PostStatus;
import io.github.martinsamuelkoleff.spring_writer.repositories.PostRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
@Sql(scripts = {"classpath:data-test.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"classpath:cleanup.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_CLASS)
public class SpringJpaTestPostRepository {

	@Autowired
	private PostRepository postRepository;
	
	final static Logger logger = LoggerFactory.getLogger(SpringJpaTestPostRepository.class);

	
	@Test
	void testCreatePost_shouldCreateNewPost() {
		Post post1 = new Post();
		post1.setTitle("New Post");
		post1.setContentMd("# Post 1");
		post1.setCreatedAt(LocalDateTime.now());
		post1.setExcerpt("new post");
		post1.setSlug("new-post");
		post1.setPublishedAt(LocalDateTime.now());
		post1.setUpdatedAt(LocalDateTime.now());
		post1.setStatus(PostStatus.PUBLISHED);
		
		post1 = postRepository.save(post1);
		
		logger.info(post1.getId().toString());
		
		Post retrieved =  postRepository.findById(post1.getId()).get();
		
		assertNotNull(retrieved);
		assertEquals(retrieved.getTitle(), post1.getTitle());
	}
	
	@Test
	void testCreatePost_shouldNotCreatePostWithSameSlug() {
		Post post1 = new Post();
		post1.setTitle("New Post");
		post1.setContentMd("# Post 1");
		post1.setCreatedAt(LocalDateTime.now());
		post1.setExcerpt("new post");
		post1.setSlug("introduccion-spring-boot"); //slug duplicado
		post1.setPublishedAt(LocalDateTime.now());
		post1.setUpdatedAt(LocalDateTime.now());
		post1.setStatus(PostStatus.PUBLISHED);
		
		
		assertThrows(Exception.class, () -> {postRepository.saveAndFlush(post1);});
	}
	
	@Test
	void testRemovePost_shouldRemovePost() {
		Optional<Post> post1 = postRepository.findById(UUID.fromString("c1b2c3d4-0001-0000-0000-000000000001"));
		
		int size = postRepository.findAll().size();
		
		assertNotNull(post1.get());
		
		post1.ifPresent( (Post post) -> {
				assertDoesNotThrow(() -> {
					postRepository.delete(post);
				});
			});
		
		assertEquals(postRepository.findAll().size(), size - 1);
	}
	
	
	@Test
	void testRetrievePosts_shouldRetrievePosts() {
		List<Post> posts = postRepository.findAll();
		assertNotNull(posts);
		assertEquals(posts.size(), 3);
	}

}
