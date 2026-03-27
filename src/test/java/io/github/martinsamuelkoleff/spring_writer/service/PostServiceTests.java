package io.github.martinsamuelkoleff.spring_writer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import io.github.martinsamuelkoleff.spring_writer.dtos.PostDTO;
import io.github.martinsamuelkoleff.spring_writer.dtos.PostMdDTO;
import io.github.martinsamuelkoleff.spring_writer.entities.Post;
import io.github.martinsamuelkoleff.spring_writer.entities.enums.PostStatus;
import io.github.martinsamuelkoleff.spring_writer.repositories.PostRepository;
import io.github.martinsamuelkoleff.spring_writer.services.PostService;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {
	
	@Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private Post publishedPost;
    private Pageable pageable;
	
    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        publishedPost = new Post();
        publishedPost.setTitle("Test Post");
        publishedPost.setSlug("test-post");
        publishedPost.setStatus(PostStatus.PUBLISHED);
        publishedPost.setContentMd("# Hello");
    }
    
    @Test
    void getPublishedPosts_returnsOnlyPublishedPosts() {
        Page<Post> repoPage = new PageImpl<>(List.of(publishedPost));
        
        when(postRepository.findByStatus(PostStatus.PUBLISHED, pageable))
            .thenReturn(repoPage);

        Page<PostDTO> result = postService.getPublishedPosts(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).slug()).isEqualTo("test-post");
        
        
        verify(postRepository).findByStatus(PostStatus.PUBLISHED, pageable);
    }

    @Test
    void getPostBySlug_whenExists_returnsDTO() {
        when(postRepository.findBySlug("test-post"))
            .thenReturn(Optional.of(publishedPost));

        Optional<PostMdDTO> result = postService.getPostBySlug("test-post");

        assertThat(result).isPresent();
        assertThat(result.get().slug()).isEqualTo("test-post");
    }

    @Test
    void getPostBySlug_whenNotExists_returnsEmpty() {
        when(postRepository.findBySlug("no-existe"))
            .thenReturn(Optional.empty());

        Optional<PostMdDTO> result = postService.getPostBySlug("no-existe");

        assertThat(result).isEmpty();
    }

    @Test
    void getPublishedPostsByCategory_filtersCorrectly() {
        UUID categoryId = UUID.randomUUID();
        Page<Post> repoPage = new PageImpl<>(List.of(publishedPost));
        
        when(postRepository.findByStatusAndCategoryId(PostStatus.PUBLISHED, categoryId, pageable))
            .thenReturn(repoPage);

        Page<PostDTO> result = postService.getPublishedPostsByCategory(categoryId, pageable);

        assertThat(result.getContent()).hasSize(1);
        
        verify(postRepository).findByStatusAndCategoryId(PostStatus.PUBLISHED, categoryId, pageable);
    }

    @Test
    void getPublishedPostsByTag_filtersCorrectly() {
        UUID tagId = UUID.randomUUID();
        Page<Post> repoPage = new PageImpl<>(List.of(publishedPost));
        when(postRepository.findByStatusAndTagsId(PostStatus.PUBLISHED, tagId, pageable))
            .thenReturn(repoPage);

        Page<PostDTO> result = postService.getPublishedPostsByTag(tagId, pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(postRepository).findByStatusAndTagsId(PostStatus.PUBLISHED, tagId, pageable);
    }
   
	
}
