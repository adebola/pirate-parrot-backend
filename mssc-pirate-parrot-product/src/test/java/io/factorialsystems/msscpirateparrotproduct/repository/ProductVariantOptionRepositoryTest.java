package io.factorialsystems.msscpirateparrotproduct.repository;


import com.github.pagehelper.Page;
import io.factorialsystems.msscpirateparrotproduct.model.ProductVariantOption;
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
class ProductVariantOptionRepositoryTest {

    @Autowired
    ProductVariantOptionRepository productVariantOptionRepository;

    @Test
    void findById() {
        final Page<ProductVariantOption> all = productVariantOptionRepository.findAll();
        List<ProductVariantOption> productVariants = all.getResult();
        ProductVariantOption pvo = productVariants.get(0);

        final ProductVariantOption pvobyId = productVariantOptionRepository.findById(pvo.getId());
        assertThat(pvobyId).isNotNull();
        assertThat(pvobyId.getId()).isEqualTo(pvo.getId());

        log.info("{}", pvobyId);
    }

    @Test
    void findByName() {
        final Page<ProductVariantOption> all = productVariantOptionRepository.findAll();
        List<ProductVariantOption> productVariants = all.getResult();
        ProductVariantOption pvo = productVariants.get(0);

        final ProductVariantOption pvobyName = productVariantOptionRepository.findByName(pvo.getName());
        assertThat(pvobyName).isNotNull();
        assertThat(pvobyName.getId()).isEqualTo(pvo.getId());

        log.info("{}", pvobyName);
    }

    @Test
    void findAll() {
        final Page<ProductVariantOption> all = productVariantOptionRepository.findAll();
        List<ProductVariantOption> productVariants = all.getResult();
        assertThat(productVariants.isEmpty()).isEqualTo(false);
    }

    @Test
    void save() {
        ProductVariantOption pvo1 = ProductVariantOption.createProductVariant("50 mg");
        ProductVariantOption pvo2 = ProductVariantOption.createProductVariant("100 mg");
        ProductVariantOption pvo3 = ProductVariantOption.createProductVariant("200 mg");

        productVariantOptionRepository.save(pvo1);
        productVariantOptionRepository.save(pvo2);
        productVariantOptionRepository.save(pvo3);

        final Page<ProductVariantOption> all = productVariantOptionRepository.findAll();
        long count = all.stream().count();
        assertThat(count).isEqualTo(6);
        log.info("Product Variant Options {}", all.getResult());
    }

    @Test
    void update() {
        final String name = "new_name";
        final Page<ProductVariantOption> all = productVariantOptionRepository.findAll();
        final ProductVariantOption pvo = all.getResult().get(0);

        //ProductVariant newPv = ProductVariant.createProductVariant(name);
    }

    @Test
    void search() {
        final Page<ProductVariantOption> search = productVariantOptionRepository.search("1");
        final List<ProductVariantOption> result = search.getResult();
        assertThat(result.size()).isGreaterThan(0);
        log.info("Size {}", result.size());
    }

    @Test
    void suspend() {
        final Page<ProductVariantOption> all = productVariantOptionRepository.findAll();
        final ProductVariantOption pvo = all.getResult().get(0);

        assertThat(pvo.getSuspended()).isEqualTo(false);
        log.info("{}", pvo);

        productVariantOptionRepository.suspend(pvo.getId());

        ProductVariantOption p = productVariantOptionRepository.findById(pvo.getId());
        assertThat(p.getId()).isEqualTo(pvo.getId());
        assertThat(p.getSuspended()).isEqualTo(true);
        log.info("{}", p);
    }

    @Test
    void unsuspend() {
        final Page<ProductVariantOption> all = productVariantOptionRepository.findAll();
        final ProductVariantOption pvo = all.getResult().get(0);

        assertThat(pvo.getSuspended()).isEqualTo(false);
        log.info("{}", pvo);

        productVariantOptionRepository.suspend(pvo.getId());

        ProductVariantOption p1 = productVariantOptionRepository.findById(pvo.getId());
        assertThat(p1.getId()).isEqualTo(pvo.getId());
        assertThat(p1.getSuspended()).isEqualTo(true);
        log.info("{}", p1);

        productVariantOptionRepository.unsuspend(p1.getId());
        ProductVariantOption p2 = productVariantOptionRepository.findById(p1.getId());
        assertThat(p2.getId()).isEqualTo(p1.getId());
        assertThat(p2.getSuspended()).isEqualTo(false);
        log.info("{}", p2);
    }
}