package com.catalog.repository;

import com.catalog.dto.CategoryDTO;
import com.catalog.entities.Category;
import com.catalog.repositories.CategoryRepository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class CategoryRepositoryTests {

    @Autowired
    EntityManager entityManager;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("Should get Category successfully from DB")
    void findCategoryByNameDocumentCase1() {
        String name = "NovaCategoria"; // nome da categoria que estamos procurando
        CategoryDTO data = new CategoryDTO(null, name); // ID nulo para permitir a geração automática
        this.createCategory(data);

        Optional<Category> result = this.categoryRepository.findByName(name);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("Should not get Category from DB when Category not exists")
    void findCategoryByNameDocumentCase2() {
        String name = "CategoriaInexistente"; // nome da categoria que estamos procurando

        Optional<Category> result = this.categoryRepository.findByName(name);

        assertThat(result.isEmpty()).isTrue();
    }

    private Category createCategory(CategoryDTO data) {
        Category category = new Category(data);
        this.entityManager.persist(category);
        return category;
    }
}