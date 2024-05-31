package io.factorialsystems.msscpirateparrotproduct.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.factorialsystems.msscpirateparrotproduct.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotproduct.dto.ProductVariantDTO;
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
class ProductVariantControllerTestIT {

    @Autowired
    ProductVariantController productVariantController;

    @Test
    @Rollback
    @Transactional
    void findAll() {
        final PagedDTO<ProductVariantDTO> all = productVariantController.findAll(1, 20);
        assertThat(all.getList().isEmpty()).isEqualTo(false);
    }

    @Test
    @Rollback
    @Transactional
    void findByName() throws JsonProcessingException {
        final String name = "dosage-1";

        final ResponseEntity<?> response = productVariantController.findByName(name);
        assertThat(response.hasBody()).isEqualTo(true);
        assertThat(response.getBody().getClass()).isEqualTo(ProductVariantDTO.class);
        final ProductVariantDTO productVariantDTO = (ProductVariantDTO) response.getBody();
        assertThat(productVariantDTO.getName()).isEqualTo(name);
        log.info("Response Body {}", response.getBody());
    }

    @Test
    @Rollback
    @Transactional
    void findById() {
        final String id = "10f084be-1f5b-11ef-800a-182d9c88d286";

        final ResponseEntity<?> response = productVariantController.findById(id);
        assertThat(response.hasBody()).isEqualTo(true);
        assertThat(response.getBody().getClass()).isEqualTo(ProductVariantDTO.class);
        final ProductVariantDTO productVariantDTO = (ProductVariantDTO) response.getBody();
        assertThat(productVariantDTO.getId()).isEqualTo(id);
        log.info("Response Body {}", response.getBody());
    }

    @Test
    @Rollback
    @Transactional
    void create() {
        final String name = "dosage-7";


        ProductVariantDTO productVariantDTO = new ProductVariantDTO();
        productVariantDTO.setName(name);

        productVariantController.create(productVariantDTO);
    }

    @Test
    @Rollback
    @Transactional
    void create_NoName_Error() {
        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            ProductVariantDTO p = new ProductVariantDTO();
            productVariantController.create(p);
        });
    }

    @Test
    @Rollback
    @Transactional
    void update() {
        final String name = "New Name";
        final String id = "10f084be-1f5b-11ef-800a-182d9c88d286";

        final ResponseEntity<?> response = productVariantController.findById(id);
        assertThat(response.hasBody()).isEqualTo(true);
        assertThat(response.getBody().getClass()).isEqualTo(ProductVariantDTO.class);
        final ProductVariantDTO p1 = (ProductVariantDTO) response.getBody();

        p1.setName(name);
        productVariantController.update(p1.getId(), p1);

        final ResponseEntity<?> response1 = productVariantController.findById(id);
        assertThat(response1.hasBody()).isEqualTo(true);
        assertThat(response1.getBody().getClass()).isEqualTo(ProductVariantDTO.class);
        final ProductVariantDTO p2 = (ProductVariantDTO) response1.getBody();

        assertThat(p2.getName()).isEqualTo(name);
    }

    @Test
    void suspend() {
        final String id = "10f084be-1f5b-11ef-800a-182d9c88d286";

        final ResponseEntity<?> response = productVariantController.findById(id);
        assertThat(response.hasBody()).isEqualTo(true);
        assertThat(response.getBody().getClass()).isEqualTo(ProductVariantDTO.class);
        final ProductVariantDTO p = (ProductVariantDTO) response.getBody();

        productVariantController.suspend(id);
    }

    @Test
    void unsuspend() {
        final String id = "10f084be-1f5b-11ef-800a-182d9c88d286";

        final ResponseEntity<?> response = productVariantController.findById(id);
        assertThat(response.hasBody()).isEqualTo(true);
        assertThat(response.getBody().getClass()).isEqualTo(ProductVariantDTO.class);
        final ProductVariantDTO p = (ProductVariantDTO) response.getBody();

        productVariantController.unsuspend(id);
    }

    @Test
    void delete() {
    }

    @Test
    void search() {
    }
}