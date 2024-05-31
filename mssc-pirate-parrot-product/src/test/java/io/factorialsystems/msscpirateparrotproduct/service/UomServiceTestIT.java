package io.factorialsystems.msscpirateparrotproduct.service;

import io.factorialsystems.msscpirateparrotproduct.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotproduct.dto.UomDTO;
import io.factorialsystems.msscpirateparrotproduct.model.Uom;
import io.factorialsystems.msscpirateparrotproduct.repository.UomRepository;
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
class UomServiceTestIT {

    @Autowired
    UomService uomService;

    @Autowired
    UomRepository uomRepository;

    @Test
    @Rollback
    @Transactional
    void clientSave() {
        final String name = "name";
        UomDTO uomDTO = new UomDTO();
        uomDTO.setName(name);

        uomService.clientSave(uomDTO);

        Optional<UomDTO> p = uomService.findByName(name);
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

        UomDTO uomDTO = new UomDTO();
        uomDTO.setName(name);

        uomService.clientSave(uomDTO);

        Optional<UomDTO> u = uomService.findByName(name);
        assertThat(u.isEmpty()).isEqualTo(false);
        assertThat(u.get().getName()).isEqualTo(name);

        log.info(u.get());

        final Optional<UomDTO> u2 = uomService.findById(u.get().getId());
        assertThat(u2.isEmpty()).isEqualTo(false);

        final UomDTO u3 = u2.get();
        u3.setName(newName);

        uomService.clientUpdate(u3.getId(), u3);
        Optional<UomDTO> u4 = uomService.findById(u3.getId());
        assertThat(u4.isEmpty()).isEqualTo(false);
        assertThat(u4.get().getName()).isEqualTo(newName);

        log.info(u4.get());
    }

    @Test
    void findAll() {
        final PagedDTO<UomDTO> all = uomService.findAll(1, 20);
        assertThat(all.getList().size()).isGreaterThan(0);
    }

    @Test
    @Rollback
    @Transactional
    void findById() {
        final String name = "dosage-7";

        UomDTO uomDTO = new UomDTO();
        uomDTO.setName(name);

        uomService.clientSave(uomDTO);
        final Optional<UomDTO> byName = uomService.findByName(name);

        assertThat(byName.isEmpty()).isEqualTo(false);
        final Optional<UomDTO> byId = uomService.findById(byName.get().getId());
        assertThat(byId.isEmpty()).isEqualTo(false);
        assertThat(byId.get().getId()).isEqualTo(byName.get().getId());

        log.info(byId);
    }

    @Test
    @Rollback
    @Transactional
    void findByName() {
        final String name = "dosage-7";

        UomDTO uomDTO = new UomDTO();
        uomDTO.setName(name);

        uomService.clientSave(uomDTO);
        final Optional<UomDTO> byName = uomService.findByName(name);

        assertThat(byName.isEmpty()).isEqualTo(false);
        assertThat(byName.get().getName()).isEqualTo(name);

        log.info(byName.get());
    }

    @Test
    void search() {
        final PagedDTO<UomDTO> an = uomService.search(1, 20, "c");
        assertThat(an.getList().isEmpty()).isEqualTo(false);
    }

    @Test
    @Rollback
    @Transactional
    void clientDelete() {
        uomService.clientDelete(UUID.randomUUID().toString());
    }

    @Test
    @Rollback
    @Transactional
    void clientSuspend() {
        final String name = "carton";

        final Optional<UomDTO> byName = uomService.findByName(name);
        assertThat(byName.isEmpty()).isEqualTo(false);
        uomService.clientSuspend(byName.get().getId());
        final Uom u = uomRepository.findById(byName.get().getId());
        assertThat(u.getSuspended()).isEqualTo(true);

        log.info(u);
    }

    @Test
    void clientUnsuspend() {
        final String name = "carton";

        final Optional<UomDTO> byName = uomService.findByName(name);
        assertThat(byName.isEmpty()).isEqualTo(false);
        uomService.clientSuspend(byName.get().getId());
        final Uom u = uomRepository.findById(byName.get().getId());
        assertThat(u.getSuspended()).isEqualTo(true);
        uomService.clientUnsuspend(byName.get().getId());

        final Uom u1 = uomRepository.findById(byName.get().getId());
        assertThat(u1.getSuspended()).isEqualTo(false);

        log.info(u1);
    }
}