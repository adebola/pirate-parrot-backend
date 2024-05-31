package io.factorialsystems.msscpirateparrotproduct.service;

import io.factorialsystems.msscpirateparrotproduct.dto.CategoryDTO;
import io.factorialsystems.msscpirateparrotproduct.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotproduct.model.Category;
import io.factorialsystems.msscpirateparrotproduct.repository.CategoryRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@CommonsLog
@SpringBootTest
class CategoryServiceTestIT {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;


    @Test
    @Rollback
    @Transactional
    void clientSave() {
        final String name = "name";

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(name);
        categoryDTO.setImageUrl("ImageUrl");

        categoryService.clientSave(categoryDTO);

        Optional<CategoryDTO> c = categoryService.findByName(name);
        assertThat(c.isEmpty()).isEqualTo(false);
        assertThat(c.get().getName()).isEqualTo(name);

        log.info(c.get());
    }

    @Test
    @Rollback
    @Transactional
    void clientUpdate() {
        final String name = "name";
        final String newName = "newName";

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(name);
        categoryDTO.setImageUrl("ImageUrl");

        categoryService.clientSave(categoryDTO);

        Optional<CategoryDTO> c = categoryService.findByName(name);
        assertThat(c.isEmpty()).isEqualTo(false);
        assertThat(c.get().getName()).isEqualTo(name);

        log.info(c.get());

        final Optional<CategoryDTO> c2 = categoryService.findById(c.get().getId());
        assertThat(c2.isEmpty()).isEqualTo(false);

        final CategoryDTO c3 = c2.get();
        c3.setName(newName);

        categoryService.clientUpdate(c3.getId(), c3);
        Optional<CategoryDTO> c4 = categoryService.findById(c3.getId());
        assertThat(c4.isEmpty()).isEqualTo(false);
        assertThat(c4.get().getName()).isEqualTo(newName);

        log.info(c4.get());
    }

    @Test
    void findAll() {
        final PagedDTO<CategoryDTO> all = categoryService.findAll(1, 20);
        assertThat(all.getList().size()).isGreaterThan(0);
    }

    @Test
    @Rollback
    @Transactional
    void findById() {
        final String name = "name1";

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(name);
        categoryDTO.setImageUrl("ImageUrl");

        categoryService.clientSave(categoryDTO);
        final Optional<CategoryDTO> byName = categoryService.findByName(name);

        assertThat(byName.isEmpty()).isEqualTo(false);
        final Optional<CategoryDTO> byId = categoryService.findById(byName.get().getId());
        assertThat(byId.isEmpty()).isEqualTo(false);
        assertThat(byId.get().getId()).isEqualTo(byName.get().getId());

        log.info(byId);
    }

    @Test
    @Rollback
    @Transactional
    void findByName() {
        final String name = "name1";

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(name);
        categoryDTO.setImageUrl("ImageUrl");

        categoryService.clientSave(categoryDTO);
        final Optional<CategoryDTO> byName = categoryService.findByName(name);

        assertThat(byName.isEmpty()).isEqualTo(false);
        assertThat(byName.get().getName()).isEqualTo(name);

        log.info(byName.get());
    }

    @Test
    @Rollback
    @Transactional
    void search() {
        final PagedDTO<CategoryDTO> an = categoryService.search(1, 20, "An");
        assertThat(an.getList().isEmpty()).isEqualTo(false);
    }

    @Test
    @Rollback
    @Transactional
    void clientDelete() {
        categoryService.clientDelete(UUID.randomUUID().toString());
    }

    @Test
    @Rollback
    @Transactional
    void clientSuspend() {
        final String name = "name";

        final Optional<CategoryDTO> byName = categoryService.findByName(name);
        assertThat(byName.isEmpty()).isEqualTo(false);
        categoryService.clientSuspend(byName.get().getId());
        final Category c = categoryRepository.findById(byName.get().getId());
        assertThat(c.getSuspended()).isEqualTo(true);

        log.info(c);
    }

    @Test
    @Rollback
    @Transactional
    void clientUnsuspend() {
        final String name = "name";

        final Optional<CategoryDTO> byName = categoryService.findByName(name);
        assertThat(byName.isEmpty()).isEqualTo(false);
        categoryService.clientSuspend(byName.get().getId());
        final Category c = categoryRepository.findById(byName.get().getId());
        assertThat(c.getSuspended()).isEqualTo(true);
        categoryService.clientUnsuspend(byName.get().getId());

        final Category c1 = categoryRepository.findById(byName.get().getId());
        assertThat(c1.getSuspended()).isEqualTo(false);

        log.info(c1);
    }
}