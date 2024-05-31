package io.factorialsystems.msscpirateparrotproduct.service;

import io.factorialsystems.msscpirateparrotproduct.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotproduct.dto.ProductVariantOptionDTO;
import io.factorialsystems.msscpirateparrotproduct.model.ProductVariantOption;
import io.factorialsystems.msscpirateparrotproduct.repository.ProductVariantOptionRepository;
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
class ProductVariantOptionServiceTestIT {

    @Autowired
    ProductVariantOptionService productVariantOptionService;

    @Autowired
    ProductVariantOptionRepository productVariantOptionRepository;

    @Test
    @Rollback
    @Transactional
    void clientSave() {
        final String name = "name";

        ProductVariantOptionDTO productVariantOptionDTO = new ProductVariantOptionDTO();
        productVariantOptionDTO.setName(name);

        productVariantOptionService.clientSave(productVariantOptionDTO);

        Optional<ProductVariantOptionDTO> p = productVariantOptionService.findByName(name);
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

        ProductVariantOptionDTO productVariantOptionDTO = new ProductVariantOptionDTO();
        productVariantOptionDTO.setName(name);

        productVariantOptionService.clientSave(productVariantOptionDTO);

        Optional<ProductVariantOptionDTO> p = productVariantOptionService.findByName(name);
        assertThat(p.isEmpty()).isEqualTo(false);
        assertThat(p.get().getName()).isEqualTo(name);

        log.info(p.get());

        final Optional<ProductVariantOptionDTO> p2 = productVariantOptionService.findById(p.get().getId());
        assertThat(p2.isEmpty()).isEqualTo(false);

        final ProductVariantOptionDTO p3 = p2.get();
        p3.setName(newName);

        productVariantOptionService.clientUpdate(p3.getId(), p3);
        Optional<ProductVariantOptionDTO> p4 = productVariantOptionService.findById(p3.getId());
        assertThat(p4.isEmpty()).isEqualTo(false);
        assertThat(p4.get().getName()).isEqualTo(newName);

        log.info(p4.get());
    }

    @Test
    void findAll() {
        final PagedDTO<ProductVariantOptionDTO> all = productVariantOptionService.findAll(1, 20);
        assertThat(all.getList().size()).isGreaterThan(0);
    }

    @Test
    @Rollback
    @Transactional
    void findById() {
        final String name = "dosage-7";

        ProductVariantOptionDTO productVariantOptionDTO = new ProductVariantOptionDTO();
        productVariantOptionDTO.setName(name);

        productVariantOptionService.clientSave(productVariantOptionDTO);
        final Optional<ProductVariantOptionDTO> byName = productVariantOptionService.findByName(name);

        assertThat(byName.isEmpty()).isEqualTo(false);
        final Optional<ProductVariantOptionDTO> byId = productVariantOptionService.findById(byName.get().getId());
        assertThat(byId.isEmpty()).isEqualTo(false);
        assertThat(byId.get().getId()).isEqualTo(byName.get().getId());

        log.info(byId);
    }

    @Test
    @Rollback
    @Transactional
    void findByName() {
        final String name = "dosage-7";

        ProductVariantOptionDTO productVariantOptionDTO = new ProductVariantOptionDTO();
        productVariantOptionDTO.setName(name);

        productVariantOptionService.clientSave(productVariantOptionDTO);
        final Optional<ProductVariantOptionDTO> byName = productVariantOptionService.findByName(name);

        assertThat(byName.isEmpty()).isEqualTo(false);
        assertThat(byName.get().getName()).isEqualTo(name);

        log.info(byName.get());
    }

    @Test
    void search() {
        final PagedDTO<ProductVariantOptionDTO> an = productVariantOptionService.search(1, 20, "1");
        assertThat(an.getList().isEmpty()).isEqualTo(false);
    }

    @Test
    @Rollback
    @Transactional
    void clientDelete() {
        productVariantOptionService.clientDelete(UUID.randomUUID().toString());
    }

    @Test
    @Rollback
    @Transactional
    void clientSuspend() {
        final String name = "15 mg";

        final Optional<ProductVariantOptionDTO> byName = productVariantOptionService.findByName(name);
        assertThat(byName.isEmpty()).isEqualTo(false);
        productVariantOptionService.clientSuspend(byName.get().getId());
        final ProductVariantOption pvo = productVariantOptionRepository.findById(byName.get().getId());
        assertThat(pvo.getSuspended()).isEqualTo(true);

        log.info(pvo);
    }

    @Test
    void clientUnsuspend() {
        final String name = "15 mg";

        final Optional<ProductVariantOptionDTO> byName = productVariantOptionService.findByName(name);
        assertThat(byName.isEmpty()).isEqualTo(false);
        productVariantOptionService.clientSuspend(byName.get().getId());
        final ProductVariantOption pvo = productVariantOptionRepository.findById(byName.get().getId());
        assertThat(pvo.getSuspended()).isEqualTo(true);
        productVariantOptionService.clientUnsuspend(byName.get().getId());

        final ProductVariantOption pvo1 = productVariantOptionRepository.findById(byName.get().getId());
        assertThat(pvo1.getSuspended()).isEqualTo(false);

        log.info(pvo1);
    }
}