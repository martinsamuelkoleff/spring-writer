package io.github.martinsamuelkoleff.spring_writer.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.github.martinsamuelkoleff.spring_writer.dtos.CommentDTO;
import io.github.martinsamuelkoleff.spring_writer.dtos.CommentPublicDTO;
import io.github.martinsamuelkoleff.spring_writer.dtos.CommentRequestDTO;
import io.github.martinsamuelkoleff.spring_writer.entities.Comment;
import io.github.martinsamuelkoleff.spring_writer.entities.enums.CommentStatus;
import io.github.martinsamuelkoleff.spring_writer.repositories.CommentRepository;
import io.github.martinsamuelkoleff.spring_writer.repositories.PostRepository;

@Service
public class CommentService {
	
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	
	public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
	}
	
	public Page<CommentPublicDTO> getApprovedCommentsByPost(UUID postId, Pageable pageable){
		return commentRepository.findByStatusAndPostId(CommentStatus.APPROVED, postId, pageable)
				.map(CommentPublicDTO::from);
	}
	
	public void savePendingComment(String slug, CommentRequestDTO request) {
		
			Comment comment = new Comment();
	        comment.setAuthorName(request.authorName());
	        comment.setAuthorEmail(request.authorEmail());
	        comment.setBody(request.body());
	        comment.setStatus(CommentStatus.PENDING);
	        postRepository.findBySlug(slug).ifPresentOrElse(post -> {
	        	comment.setPost(post);
	        }, () -> {throw new IllegalArgumentException("Id no valido");});
	           
	        commentRepository.save(comment);
	}

	public void approve(UUID commentId) {
		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {throw new IllegalArgumentException("Id no valido");});
		comment.setStatus(CommentStatus.APPROVED);
		commentRepository.save(comment);
	}

	public void delete(UUID commentId) {
		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {throw new IllegalArgumentException("Id no valido");});
		commentRepository.delete(comment);
	}

	public Page<CommentDTO> getCommentsByPost(UUID id, Pageable pageable) {
		return commentRepository.findAllByPostId(id, pageable).map(CommentDTO::from);
	}
	
	public Page<CommentDTO> getPendingComments(Pageable pageable){
		return commentRepository.findAllByStatus(CommentStatus.PENDING, pageable).map(CommentDTO::from);
	}
}
