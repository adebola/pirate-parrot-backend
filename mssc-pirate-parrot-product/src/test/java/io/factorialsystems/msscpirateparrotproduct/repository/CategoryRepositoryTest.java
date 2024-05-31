package io.factorialsystems.msscpirateparrotproduct.repository;

import com.github.pagehelper.Page;
import io.factorialsystems.msscpirateparrotproduct.model.Category;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void findAll() {
        final Page<Category> all = categoryRepository.findAll();
        long count = all.size();
        assertThat(count).isEqualTo(5);
        log.info("Categories {}", all.getResult());
    }

    @Test
    void findById() {
        final Page<Category> all = categoryRepository.findAll();
        List<Category> categories = all.getResult();
        Category c = categories.get(0);

        final Category byId = categoryRepository.findById(c.getId());
        assertThat(byId).isNotNull();
        assertThat(byId.getId()).isEqualTo(c.getId());

        log.info("{}", byId);
    }

    @Test
    void save() {
        Category c1 = Category.createCategory("Bipolar agents", "Analgesics");
        Category c2 = Category.createCategory("Antidementia agents", "Antidementia agents");
        Category c3 = Category.createCategory("Hormone suppressant (thyroid)", "Hormone suppressant (thyroid)");

        categoryRepository.save(c1);
        categoryRepository.save(c2);
        categoryRepository.save(c3);

        final Page<Category> all = categoryRepository.findAll();
        long count = all.stream().count();
        assertThat(count).isEqualTo(8);
        log.info("Categories {}", all.getResult());
    }

    @Test
    void update() {
        final String name = "new_name";
        final Page<Category> all = categoryRepository.findAll();
        final Category category = all.getResult().get(0);

        Category c = Category.createCategory(name, category.getImageUrl());
        categoryRepository.update(c);

//        Category newCategory = categoryRepository.findById(category.getId());
//        assertThat(newCategory.getName()).isEqualTo(name);
//        assertThat(newCategory.getId()).isEqualTo(category.getId());
    }

    @Test
    void suspend() {
        final Page<Category> all = categoryRepository.findAll();
        final Category category = all.getResult().get(0);

        assertThat(category.getSuspended()).isEqualTo(false);
        log.info("{}", category);

        categoryRepository.suspend(category.getId());

        Category c = categoryRepository.findById(category.getId());
        assertThat(c.getId()).isEqualTo(category.getId());
        assertThat(c.getSuspended()).isEqualTo(true);
        log.info("{}", c);
    }

    @Test
    void unsuspend() {
        final Page<Category> all = categoryRepository.findAll();
        final Category category = all.getResult().get(0);

        assertThat(category.getSuspended()).isEqualTo(false);
        log.info("{}", category);

        categoryRepository.suspend(category.getId());

        Category c = categoryRepository.findById(category.getId());
        assertThat(c.getId()).isEqualTo(category.getId());
        assertThat(c.getSuspended()).isEqualTo(true);
        log.info("{}", c);

        categoryRepository.unsuspend(c.getId());
        Category c1 = categoryRepository.findById(c.getId());
        assertThat(c1.getId()).isEqualTo(c.getId());
        assertThat(c1.getSuspended()).isEqualTo(false);
        log.info("{}", c1);
    }

    @Test
    void search() {
        final Page<Category> search = categoryRepository.search("Ana");
        final List<Category> result = search.getResult();
        assertThat(result.size()).isGreaterThan(0);
        log.info("Size {}", result.size());
    }
}