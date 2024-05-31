package io.factorialsystems.msscpirateparrotproduct.repository;

import com.github.pagehelper.Page;
import io.factorialsystems.msscpirateparrotproduct.model.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryRepository {
    Page<Category> findAll();
    Category findById(String id);
    Category findByName(String name);
    void save(Category category);
    void update(Category category);
    void suspend(String id);
    void unsuspend(String id);
    Page<Category> search(String search);
}