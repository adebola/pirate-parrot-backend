package io.factorialsystems.msscpirateparrotproduct.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.factorialsystems.msscpirateparrotproduct.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotproduct.dto.UomDTO;
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
class UomControllerTestIT {
    @Autowired
    UomController uomController;

    @Test
    @Rollback
    @Transactional
    void findAll() {
        final PagedDTO<UomDTO> all = uomController.findAll(1, 20);
        assertThat(all.getList().isEmpty()).isEqualTo(false);
    }

    @Test
    @Rollback
    @Transactional
    void findByName() throws JsonProcessingException {
        final String name = "carton";

        final ResponseEntity<?> response = uomController.findByName(name);
        assertThat(response.hasBody()).isEqualTo(true);
        assertThat(response.getBody().getClass()).isEqualTo(UomDTO.class);
        final UomDTO uomDTO = (UomDTO) response.getBody();
        assertThat(uomDTO.getName()).isEqualTo(name);
        log.info("Response Body {}", response.getBody());
    }

    @Test
    @Rollback
    @Transactional
    void findById() {
        final String id = "8e8d9612-1f85-11ef-800a-182d9c88d286";

        final ResponseEntity<?> response = uomController.findById(id);
        assertThat(response.hasBody()).isEqualTo(true);
        assertThat(response.getBody().getClass()).isEqualTo(UomDTO.class);
        final UomDTO uomDTO = (UomDTO) response.getBody();
        assertThat(uomDTO.getId()).isEqualTo(id);
        log.info("Response Body {}", response.getBody());
    }

    @Test
    @Rollback
    @Transactional
    void create() {
        final String name = "name1";

        UomDTO uomDTO = new UomDTO();
        uomDTO.setName(name);
        uomController.create(uomDTO);
    }

    @Test
    @Rollback
    @Transactional
    void create_NoName_Error() {
        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            final String imageUrl = "imageIUrl";

            UomDTO uomDTO = new UomDTO();
            uomController.create(uomDTO);
        });
    }

    @Test
    @Rollback
    @Transactional
    void update() {
        final String name = "xyz";
        final String id = "8e8d9612-1f85-11ef-800a-182d9c88d286";

        final ResponseEntity<?> response = uomController.findById(id);
        assertThat(response.hasBody()).isEqualTo(true);
        assertThat(response.getBody().getClass()).isEqualTo(UomDTO.class);
        final UomDTO uomDTO = (UomDTO) response.getBody();

        uomDTO.setName(name);
        uomController.update(uomDTO.getId(), uomDTO);

        final ResponseEntity<?> response1 = uomController.findById(id);
        assertThat(response1.hasBody()).isEqualTo(true);
        assertThat(response1.getBody().getClass()).isEqualTo(UomDTO.class);
        final UomDTO uomDTO1 = (UomDTO) response1.getBody();

        assertThat(uomDTO1.getName()).isEqualTo(name);
    }

    @Test
    void suspend() {
        final String id = "8e8d9612-1f85-11ef-800a-182d9c88d286";

        final ResponseEntity<?> response = uomController.findById(id);
        assertThat(response.hasBody()).isEqualTo(true);
        assertThat(response.getBody().getClass()).isEqualTo(UomDTO.class);
        final UomDTO uomDTO = (UomDTO) response.getBody();

        uomController.suspend(id);
    }

    @Test
    void unsuspend() {
        final String id = "8e8d9612-1f85-11ef-800a-182d9c88d286";

        final ResponseEntity<?> response = uomController.findById(id);
        assertThat(response.hasBody()).isEqualTo(true);
        assertThat(response.getBody().getClass()).isEqualTo(UomDTO.class);
        final UomDTO uomDTO = (UomDTO) response.getBody();

        uomController.unsuspend(id);
    }

    @Test
    void delete() {
    }

    @Test
    void search() {
    }
}