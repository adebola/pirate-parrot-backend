package io.factorialsystems.msscpirateparrotproduct.service;

import io.factorialsystems.msscpirateparrotproduct.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotproduct.dto.ProductVariantDTO;
import io.factorialsystems.msscpirateparrotproduct.model.ProductVariant;
import io.factorialsystems.msscpirateparrotproduct.repository.ProductVariantRepository;
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
class ProductVariantServiceTestIT {

    @Autowired
    ProductVariantService productVariantService;

    @Autowired
    ProductVariantRepository productVariantRepository;

    @Test
    @Rollback
    @Transactional
    void clientSave() {
        final String name = "name";

        ProductVariantDTO productVariantDTO = new ProductVariantDTO();
        productVariantDTO.setName(name);

        productVariantService.clientSave(productVariantDTO);

        Optional<ProductVariantDTO> p = productVariantService.findByName(name);
        assertThat(p.isEmpty()).isEqualTo(false);
        assertThat(p.get().getName()).isEqualTo(name);

        log.info(p.get());
    }

    @Test
    @Rollback
    @Transactional
    void clientUpdate() {
        final String name = "name";
        final String newName = "newName";

        ProductVariantDTO productVariantDTO = new ProductVariantDTO();
        productVariantDTO.setName(name);

        productVariantService.clientSave(productVariantDTO);

        Optional<ProductVariantDTO> p = productVariantService.findByName(name);
        assertThat(p.isEmpty()).isEqualTo(false);
        assertThat(p.get().getName()).isEqualTo(name);

        log.info(p.get());

        final Optional<ProductVariantDTO> p2 = productVariantService.findById(p.get().getId());
        assertThat(p2.isEmpty()).isEqualTo(false);

        final ProductVariantDTO p3 = p2.get();
        p3.setName(newName);

        productVariantService.clientUpdate(p3.getId(), p3);
        Optional<ProductVariantDTO> p4 = productVariantService.findById(p3.getId());
        assertThat(p4.isEmpty()).isEqualTo(false);
        assertThat(p4.get().getName()).isEqualTo(newName);

        log.info(p4.get());
    }

    @Test
    void findAll() {
        final PagedDTO<ProductVariantDTO> all = productVariantService.findAll(1, 20);
        assertThat(all.getList().size()).isGreaterThan(0);
    }

    @Test
    @Rollback
    @Transactional
    void findById() {
        final String name = "dosage-7";

        ProductVariantDTO productVariantDTO = new ProductVariantDTO();
        productVariantDTO.setName(name);

        productVariantService.clientSave(productVariantDTO);
        final Optional<ProductVariantDTO> byName = productVariantService.findByName(name);

        assertThat(byName.isEmpty()).isEqualTo(false);
        final Optional<ProductVariantDTO> byId = productVariantService.findById(byName.get().getId());
        assertThat(byId.isEmpty()).isEqualTo(false);
        assertThat(byId.get().getId()).isEqualTo(byName.get().getId());

        log.info(byId);
    }

    @Test
    @Rollback
    @Transactional
    void findByName() {
        final String name = "dosage-7";

        ProductVariantDTO productVariantDTO = new ProductVariantDTO();
        productVariantDTO.setName(name);

        productVariantService.clientSave(productVariantDTO);
        final Optional<ProductVariantDTO> byName = productVariantService.findByName(name);

        assertThat(byName.isEmpty()).isEqualTo(false);
        assertThat(byName.get().getName()).isEqualTo(name);

        log.info(byName.get());
    }

    @Test
    void search() {
        final PagedDTO<ProductVariantDTO> an = productVariantService.search(1, 20, "do");
        assertThat(an.getList().isEmpty()).isEqualTo(false);
    }

    @Test
    @Rollback
    @Transactional
    void clientDelete() {
        productVariantService.clientDelete(UUID.randomUUID().toString());
    }

    @Test
    @Rollback
    @Transactional
    void clientSuspend() {
        final String name = "dosage-1";

        final Optional<ProductVariantDTO> byName = productVariantService.findByName(name);
        assertThat(byName.isEmpty()).isEqualTo(false);
        productVariantService.clientSuspend(byName.get().getId());
        final ProductVariant pv = productVariantRepository.findById(byName.get().getId());
        assertThat(pv.getSuspended()).isEqualTo(true);

        log.info(pv);
    }

    @Test
    void clientUnsuspend() {
        final String name = "dosage-1";

        final Optional<ProductVariantDTO> byName = productVariantService.findByName(name);
        assertThat(byName.isEmpty()).isEqualTo(false);
        productVariantService.clientSuspend(byName.get().getId());
        final ProductVariant pv = productVariantRepository.findById(byName.get().getId());
        assertThat(pv.getSuspended()).isEqualTo(true);
        productVariantService.clientUnsuspend(byName.get().getId());

        final ProductVariant pv1 = productVariantRepository.findById(byName.get().getId());
        assertThat(pv1.getSuspended()).isEqualTo(false);

        log.info(pv1);
    }
}