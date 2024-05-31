package io.factorialsystems.msscpirateparrotproduct.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.factorialsystems.msscpirateparrotproduct.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotproduct.dto.ProductVariantOptionDTO;
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
class ProductVariantOptionControllerTestIT {
    @Autowired
    ProductVariantOptionController productVariantOptionController;

    @Test
    @Rollback
    @Transactional
    void findAll() {
        final PagedDTO<ProductVariantOptionDTO> all = productVariantOptionController.findAll(1, 20);
        assertThat(all.getList().isEmpty()).isEqualTo(false);
    }

    @Test
    @Rollback
    @Transactional
    void findByName() throws JsonProcessingException {
        final String name = "15 mg";

        final ResponseEntity<?> response = productVariantOptionController.findByName(name);
        assertThat(response.hasBody()).isEqualTo(true);
        assertThat(response.getBody().getClass()).isEqualTo(ProductVariantOptionDTO.class);
        final ProductVariantOptionDTO productVariantOptionDTO = (ProductVariantOptionDTO) response.getBody();
        assertThat(productVariantOptionDTO.getName()).isEqualTo(name);
        log.info("Response Body {}", response.getBody());
    }

    @Test
    @Rollback
    @Transactional
    void findById() {
        final String id = "009da136-1f7a-11ef-800a-182d9c88d286";

        final ResponseEntity<?> response = productVariantOptionController.findById(id);
        assertThat(response.hasBody()).isEqualTo(true);
        assertThat(response.getBody().getClass()).isEqualTo(ProductVariantOptionDTO.class);
        final ProductVariantOptionDTO productVariantOptionDTO = (ProductVariantOptionDTO) response.getBody();
        assertThat(productVariantOptionDTO.getId()).isEqualTo(id);
        log.info("Response Body {}", response.getBody());
    }

    @Test
    @Rollback
    @Transactional
    void create() {
        final String name = "75 mg";

        ProductVariantOptionDTO productVariantOptionDTO = new ProductVariantOptionDTO();
        productVariantOptionDTO.setName(name);
        productVariantOptionController.create(productVariantOptionDTO);
    }

    @Test
    @Rollback
    @Transactional
    void create_NoName_Error() {
        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            ProductVariantOptionDTO p = new ProductVariantOptionDTO();
            productVariantOptionController.create(p);
        });
    }

    @Test
    @Rollback
    @Transactional
    void update() {
        final String name = "New Name";
        final String id = "009da136-1f7a-11ef-800a-182d9c88d286";

        final ResponseEntity<?> response = productVariantOptionController.findById(id);
        assertThat(response.hasBody()).isEqualTo(true);
        assertThat(response.getBody().getClass()).isEqualTo(ProductVariantOptionDTO.class);
        final ProductVariantOptionDTO p1 = (ProductVariantOptionDTO) response.getBody();

        p1.setName(name);
        productVariantOptionController.update(p1.getId(), p1);

        final ResponseEntity<?> response1 = productVariantOptionController.findById(id);
        assertThat(response1.hasBody()).isEqualTo(true);
        assertThat(response1.getBody().getClass()).isEqualTo(ProductVariantOptionDTO.class);
        final ProductVariantOptionDTO p2 = (ProductVariantOptionDTO) response1.getBody();

        assertThat(p2.getName()).isEqualTo(name);
    }

    @Test
    void suspend() {
        final String id = "009da136-1f7a-11ef-800a-182d9c88d286";

        final ResponseEntity<?> response = productVariantOptionController.findById(id);
        assertThat(response.hasBody()).isEqualTo(true);
        assertThat(response.getBody().getClass()).isEqualTo(ProductVariantOptionDTO.class);
        final ProductVariantOptionDTO p = (ProductVariantOptionDTO) response.getBody();

        productVariantOptionController.suspend(id);
    }

    @Test
    void unsuspend() {
        final String id = "009da136-1f7a-11ef-800a-182d9c88d286";

        final ResponseEntity<?> response = productVariantOptionController.findById(id);
        assertThat(response.hasBody()).isEqualTo(true);
        assertThat(response.getBody().getClass()).isEqualTo(ProductVariantOptionDTO.class);
        final ProductVariantOptionDTO p = (ProductVariantOptionDTO) response.getBody();

        productVariantOptionController.unsuspend(id);
    }

    @Test
    void delete() {
    }

    @Test
    void search() {
    }
}