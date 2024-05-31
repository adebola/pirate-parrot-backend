package io.factorialsystems.msscpirateparrotproduct.repository;

import com.github.pagehelper.Page;
import io.factorialsystems.msscpirateparrotproduct.model.ProductVariant;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductVariantRepository {
    ProductVariant findById(String id);
    ProductVariant findByName(String name);
    Page<ProductVariant> findAll();
    void save(ProductVariant pv);
    void update(ProductVariant pv);
    Page<ProductVariant> search(String search);
    int suspend(String id);
    int unsuspend(String id);
}
