package io.factorialsystems.msscpirateparrotproduct.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.factorialsystems.msscpirateparrotproduct.dto.CategoryDTO;
import io.factorialsystems.msscpirateparrotproduct.dto.PagedDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
class CategoryControllerTestIT {

    @Autowired
    CategoryController categoryController;

    @Test
    @Rollback
    @Transactional
    void findAll() {
        final PagedDTO<CategoryDTO> all = categoryController.findAll(1, 20);
        assertThat(all.getList().isEmpty()).isEqualTo(false);
    }

    @Test
    @Rollback
    @Transactional
    void findByName() throws JsonProcessingException {
        final String name = "name";

        final ResponseEntity<?> response = categoryController.findByName(name);
        assertThat(response.hasBody()).isEqualTo(true);
        assertThat(response.getBody().getClass()).isEqualTo(CategoryDTO.class);
        final CategoryDTO categoryDTO = (CategoryDTO) response.getBody();
        assertThat(categoryDTO.getName()).isEqualTo(name);
        log.info("Response Body {}", response.getBody());
    }

    @Test
    @Rollback
    @Transactional
    void findById() {
        final String id = "38076236-1f40-11ef-800a-182d9c88d286";

        final ResponseEntity<?> response = categoryController.findById(id);
        assertThat(response.hasBody()).isEqualTo(true);
        assertThat(response.getBody().getClass()).isEqualTo(CategoryDTO.class);
        final CategoryDTO categoryDTO = (CategoryDTO) response.getBody();
        assertThat(categoryDTO.getId()).isEqualTo(id);
        log.info("Response Body {}", response.getBody());
    }

    @Test
    @Rollback
    @Transactional
    void create() {
        final String name = "name1";
        final String imageUrl = "imageIUrl";

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(name);
        categoryDTO.setImageUrl(imageUrl);

        categoryController.create(categoryDTO);
    }

    @Test
    @Rollback
    @Transactional
    void create_NoName_Error() {
        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            final String imageUrl = "imageIUrl";

            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setImageUrl(imageUrl);
            categoryController.create(categoryDTO);
        });
    }

    @Test
    @Rollback
    @Transactional
    void update() {
        final String imageUrl = "New ImageUrl";
        final String id = "38076236-1f40-11ef-800a-182d9c88d286";

        final ResponseEntity<?> response = categoryController.findById(id);
        assertThat(response.hasBody()).isEqualTo(true);
        assertThat(response.getBody().getClass()).isEqualTo(CategoryDTO.class);
        final CategoryDTO categoryDTO = (CategoryDTO) response.getBody();

        categoryDTO.setImageUrl(imageUrl);
        categoryController.update(categoryDTO.getId(), categoryDTO);

        final ResponseEntity<?> response1 = categoryController.findById(id);
        assertThat(response1.hasBody()).isEqualTo(true);
        assertThat(response1.getBody().getClass()).isEqualTo(CategoryDTO.class);
        final CategoryDTO categoryDTO1 = (CategoryDTO) response1.getBody();

        assertThat(categoryDTO1.getImageUrl()).isEqualTo(imageUrl);
    }

    @Test
    void suspend() {
        final String id = "38076236-1f40-11ef-800a-182d9c88d286";

        final ResponseEntity<?> response = categoryController.findById(id);
        assertThat(response.hasBody()).isEqualTo(true);
        assertThat(response.getBody().getClass()).isEqualTo(CategoryDTO.class);
        final CategoryDTO categoryDTO = (CategoryDTO) response.getBody();

        categoryController.suspend(id);
    }

    @Test
    void unsuspend() {
        final String id = "38076236-1f40-11ef-800a-182d9c88d286";

        final ResponseEntity<?> response = categoryController.findById(id);
        assertThat(response.hasBody()).isEqualTo(true);
        assertThat(response.getBody().getClass()).isEqualTo(CategoryDTO.class);
        final CategoryDTO categoryDTO = (CategoryDTO) response.getBody();

        categoryController.unsuspend(id);
    }

    @Test
    void delete() {
    }

    @Test
    void search() {
    }
}