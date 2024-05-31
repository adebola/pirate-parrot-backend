package io.factorialsystems.msscpirateparrotproduct.repository;

import com.github.pagehelper.Page;
import io.factorialsystems.msscpirateparrotproduct.model.ProductVariantOption;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductVariantOptionRepository {
    ProductVariantOption findById(String id);
    ProductVariantOption findByName(String name);
    Page<ProductVariantOption> findAll();
    void save(ProductVariantOption pvo);
    void update(ProductVariantOption pv);
    Page<ProductVariantOption> search(String search);
    int suspend(String id);
    int unsuspend(String id);
}
