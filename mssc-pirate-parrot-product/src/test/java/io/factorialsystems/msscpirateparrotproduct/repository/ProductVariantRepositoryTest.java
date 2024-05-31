package io.factorialsystems.msscpirateparrotproduct.repository;

import com.github.pagehelper.Page;
import io.factorialsystems.msscpirateparrotproduct.model.ProductVariant;
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
class ProductVariantRepositoryTest {

    @Autowired
    ProductVariantRepository productVariantRepository;

    @Test
    void findById() {
        final Page<ProductVariant> all = productVariantRepository.findAll();
        List<ProductVariant> productVariants = all.getResult();
        ProductVariant pv = productVariants.get(0);

        final ProductVariant pvbyId = productVariantRepository.findById(pv.getId());
        assertThat(pvbyId).isNotNull();
        assertThat(pvbyId.getId()).isEqualTo(pv.getId());

        log.info("{}", pvbyId);
    }

    @Test
    void findByName() {
        final Page<ProductVariant> all = productVariantRepository.findAll();
        List<ProductVariant> productVariants = all.getResult();
        ProductVariant pv = productVariants.get(0);

        final ProductVariant pvbyName = productVariantRepository.findByName(pv.getName());
        assertThat(pvbyName).isNotNull();
        assertThat(pvbyName.getId()).isEqualTo(pv.getId());

        log.info("{}", pvbyName);
    }

    @Test
    void findAll() {
        final Page<ProductVariant> all = productVariantRepository.findAll();
        List<ProductVariant> productVariants = all.getResult();
        assertThat(productVariants.isEmpty()).isEqualTo(false);
    }

    @Test
    void save() {
        ProductVariant pv1 = ProductVariant.createProductVariant("Dosage-4");
        ProductVariant pv2 = ProductVariant.createProductVariant("Dosage-5");
        ProductVariant pv3 = ProductVariant.createProductVariant("Dosage-6");

        productVariantRepository.save(pv1);
        productVariantRepository.save(pv2);
        productVariantRepository.save(pv3);

        final Page<ProductVariant> all = productVariantRepository.findAll();
        long count = all.stream().count();
        assertThat(count).isEqualTo(7);
        log.info("Product Variants {}", all.getResult());
    }

    @Test
    void update() {
        final String name = "new_name";
        final Page<ProductVariant> all = productVariantRepository.findAll();
        final ProductVariant pv = all.getResult().get(0);

        ProductVariant newPv = ProductVariant.createProductVariant(name);
    }

    @Test
    void search() {
        final Page<ProductVariant> search = productVariantRepository.search("do");
        final List<ProductVariant> result = search.getResult();
        assertThat(result.size()).isGreaterThan(0);
        log.info("Size {}", result.size());
    }

    @Test
    void suspend() {
        final Page<ProductVariant> all = productVariantRepository.findAll();
        final ProductVariant pv = all.getResult().get(0);

        assertThat(pv.getSuspended()).isEqualTo(false);
        log.info("{}", pv);

        productVariantRepository.suspend(pv.getId());

        ProductVariant p = productVariantRepository.findById(pv.getId());
        assertThat(p.getId()).isEqualTo(pv.getId());
        assertThat(p.getSuspended()).isEqualTo(true);
        log.info("{}", p);
    }

    @Test
    void unsuspend() {
        final Page<ProductVariant> all = productVariantRepository.findAll();
        final ProductVariant pv = all.getResult().get(0);

        assertThat(pv.getSuspended()).isEqualTo(false);
        log.info("{}", pv);

        productVariantRepository.suspend(pv.getId());

        ProductVariant p1 = productVariantRepository.findById(pv.getId());
        assertThat(p1.getId()).isEqualTo(pv.getId());
        assertThat(p1.getSuspended()).isEqualTo(true);
        log.info("{}", p1);

        productVariantRepository.unsuspend(p1.getId());
        ProductVariant p2 = productVariantRepository.findById(p1.getId());
        assertThat(p2.getId()).isEqualTo(p1.getId());
        assertThat(p2.getSuspended()).isEqualTo(false);
        log.info("{}", p2);
    }
}