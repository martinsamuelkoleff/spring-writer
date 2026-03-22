package io.github.martinsamuelkoleff.spring_writer.jpa;

import io.github.martinsamuelkoleff.spring_writer.entities.Category;
import io.github.martinsamuelkoleff.spring_writer.repositories.CategoryRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class SpringJpaTestCategoryRepository {

	@Autowired
    private CategoryRepository categoryRepository;
	
	@Test
	void shouldPersistCategory() {
		Category category = new Category();
        category.setName("category 1");
        category.setSlug("category-1");

        Category saved = categoryRepository.save(category);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("category 1");
	}
	
	@Test
	void shouldNotPersistCategory_whenSlugIsDuplicated() {
	    Category c1 = new Category();
	    c1.setName("category 1");
	    c1.setSlug("category-1");

	    Category c2 = new Category();
	    c2.setName("category 2");
	    c2.setSlug("category-1");

	    categoryRepository.saveAndFlush(c1);

	    assertThrows(Exception.class, () -> {
	        categoryRepository.saveAndFlush(c2);
	    });
	}
	
}
