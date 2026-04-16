package io.github.martinsamuelkoleff.spring_writer.config;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.github.martinsamuelkoleff.spring_writer.entities.Category;
import io.github.martinsamuelkoleff.spring_writer.entities.Comment;
import io.github.martinsamuelkoleff.spring_writer.entities.Post;
import io.github.martinsamuelkoleff.spring_writer.entities.Tag;
import io.github.martinsamuelkoleff.spring_writer.entities.enums.CommentStatus;
import io.github.martinsamuelkoleff.spring_writer.entities.enums.PostStatus;
import io.github.martinsamuelkoleff.spring_writer.repositories.CategoryRepository;
import io.github.martinsamuelkoleff.spring_writer.repositories.CommentRepository;
import io.github.martinsamuelkoleff.spring_writer.repositories.PostRepository;
import io.github.martinsamuelkoleff.spring_writer.repositories.TagRepository;

@Configuration
@Profile("dev")
public class DataInitializer {
	
	@Bean @Profile("dev")
	CommandLineRunner initData(CategoryRepository categoryRepo, TagRepository tagRepo, PostRepository postRepo, CommentRepository commentRepo) {
		return args -> {
			if (categoryRepo.count() == 0) {
				categoryRepo.saveAll(
						List.of(new Category(null, "Tutorial", "tutorial"), new Category(null, "Guía", "guia"), new Category(null, "Artículo", "articulo"), new Category(null, "Caso real", "caso-real"), new Category(null, "Opinión", "opinion") )); 
				} 
			
			if (tagRepo.count() == 0) {
				tagRepo.saveAll(List.of( new Tag(null, "Spring Boot", "spring-boot"), new Tag(null, "Spring Framework", "spring-framework"), new Tag(null, "Spring MVC", "spring-mvc"), new Tag(null, "Spring Security", "spring-security"), new Tag(null, "Spring JPA", "spring-jpa"), new Tag(null, "Hibernate", "hibernate"), new Tag(null, "MySQL", "mysql"), new Tag(null, "JUnit", "junit"), new Tag(null, "Mockito", "mockito"), new Tag(null, "Jakarta", "jakarta"), new Tag(null, "Java", "java"), new Tag(null, "Docker", "docker") )); 
			}
			
			 if (postRepo.count() == 0) {

		            List<Category> categories = categoryRepo.findAll();
		            List<Tag> tags = tagRepo.findAll();

		            for (int i = 1; i <= 50; i++) {

		                Post post = new Post();

		                String title = "Post de prueba " + i;
		                String slug = title.toLowerCase().replace(" ", "-");

		                post.setTitle(title);
		                post.setSlug(slug);
		                post.setContentMd(generateContent());
		                post.setExcerpt("Resumen del " + title);

		                post.setStatus(PostStatus.PUBLISHED);
		                post.setPublishedAt(LocalDateTime.now().minusDays(i));

		                Category randomCategory = categories.get((int) (Math.random() * categories.size()));
		                post.setCategory(randomCategory);

		                Set<Tag> randomTags = new HashSet<>();
		                int cantidadTags = 1 + (int) (Math.random() * 3);

		                for (int j = 0; j < cantidadTags; j++) {
		                    Tag randomTag = tags.get((int) (Math.random() * tags.size()));
		                    randomTags.add(randomTag);
		                }

		                post.setTags(randomTags);

		                postRepo.save(post);
		            }
		        }
			 
			 if (commentRepo.count() == 0) {

			     List<Post> posts = postRepo.findAll();

			     for (Post post : posts) {

			         int cantidad = 2 + (int) (Math.random() * 5);

			         for (int i = 0; i < cantidad; i++) {

			             Comment comment = new Comment();

			             comment.setAuthorName(randomName());
			             comment.setAuthorEmail(randomEmail());
			             comment.setBody(generateComment());

			             CommentStatus[] statuses = CommentStatus.values();
			             comment.setStatus(statuses[(int) (Math.random() * statuses.length)]);

			             comment.setPost(post);

			             comment.setCreatedAt(
			                 LocalDateTime.now().minusDays((int) (Math.random() * 30))
			             );

			             commentRepo.save(comment);
			         }
			     }
			 }
		};
		
		
	
	}
	
	private String generateContent() {
	    return """
	            # Título de ejemplo
	            
	            Este es un post generado automáticamente para pruebas.
	            
	            ## Sección
	            
	            Lorem ipsum dolor sit amet, consectetur adipiscing elit.
	            Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
	            
	            ## Código
	            
	            ```java
	            System.out.println("Hola mundo");
	            ```
	            
	            Fin del contenido.
	            """;
	}
	
	private String randomName() {
	    String[] nombres = {"Juan", "María", "Pedro", "Lucía", "Carlos", "Ana"};
	    String[] apellidos = {"Pérez", "Gómez", "López", "Díaz", "Fernández"};

	    return nombres[(int) (Math.random() * nombres.length)] + " " +
	           apellidos[(int) (Math.random() * apellidos.length)];
	}
	
	private String randomEmail() {
	    String[] dominios = {"gmail.com", "hotmail.com", "test.com"};

	    return "user" + (int) (Math.random() * 1000) + "@" +
	           dominios[(int) (Math.random() * dominios.length)];
	}
	
	private String generateComment() {
	    String[] comments = {
	        "Excelente artículo, me sirvió mucho.",
	        "No estoy de acuerdo con este enfoque.",
	        "Muy bien explicado, gracias!",
	        "Podrías profundizar más en este tema.",
	        "Esto me ahorró horas de trabajo.",
	        "Gran contenido 👏"
	    };

	    return comments[(int) (Math.random() * comments.length)];
	}
}