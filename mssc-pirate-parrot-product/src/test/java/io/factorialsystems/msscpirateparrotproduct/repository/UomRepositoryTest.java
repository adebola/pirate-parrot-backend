package io.factorialsystems.msscpirateparrotproduct.repository;

import com.github.pagehelper.Page;
import io.factorialsystems.msscpirateparrotproduct.model.Uom;
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
class UomRepositoryTest {

    @Autowired
    UomRepository uomRepository;

    @Test
    void findById() {
        final Page<Uom> all = uomRepository.findAll();
        List<Uom> uoms = all.getResult();
        Uom uom = uoms.get(0);

        final Uom uombyId = uomRepository.findById(uom.getId());
        assertThat(uombyId).isNotNull();
        assertThat(uombyId.getId()).isEqualTo(uom.getId());

        log.info("{}", uombyId);
    }

    @Test
    void findByName() {
        final Page<Uom> all = uomRepository.findAll();
        List<Uom> uoms = all.getResult();
        Uom pv = uoms.get(0);

        final Uom pvbyName = uomRepository.findByName(pv.getName());
        assertThat(pvbyName).isNotNull();
        assertThat(pvbyName.getId()).isEqualTo(pv.getId());

        log.info("{}", pvbyName);
    }

    @Test
    void findAll() {
        final Page<Uom> all = uomRepository.findAll();
        List<Uom> uoms = all.getResult();
        assertThat(uoms.isEmpty()).isEqualTo(false);
    }

    @Test
    void save() {
        Uom u1 = Uom.createUom("uom-1");
        Uom u2 = Uom.createUom("uom-2");
        Uom u3 = Uom.createUom("uom-3");

        uomRepository.save(u1);
        uomRepository.save(u2);
        uomRepository.save(u3);

        final Page<Uom> all = uomRepository.findAll();
        long count = all.stream().count();
        assertThat(count).isEqualTo(6);
        log.info("Uom {}", all.getResult());
    }

    @Test
    void update() {
        final String name = "new_name";
        final Page<Uom> all = uomRepository.findAll();
        final Uom u = all.getResult().get(0);

        //Uom newPv = Uom.createUom(name);
    }

    @Test
    void search() {
        final Page<Uom> search = uomRepository.search("c");
        final List<Uom> result = search.getResult();
        assertThat(result.size()).isGreaterThan(0);
        log.info("Size {}", result.size());
    }

    @Test
    void suspend() {
        final Page<Uom> all = uomRepository.findAll();
        final Uom u = all.getResult().get(0);

        assertThat(u.getSuspended()).isEqualTo(false);
        log.info("{}", u);

        uomRepository.suspend(u.getId());

        Uom p = uomRepository.findById(u.getId());
        assertThat(p.getId()).isEqualTo(u.getId());
        assertThat(p.getSuspended()).isEqualTo(true);
        log.info("{}", p);
    }

    @Test
    void unsuspend() {
        final Page<Uom> all = uomRepository.findAll();
        final Uom u = all.getResult().get(0);

        assertThat(u.getSuspended()).isEqualTo(false);
        log.info("{}", u);

        uomRepository.suspend(u.getId());

        Uom p1 = uomRepository.findById(u.getId());
        assertThat(p1.getId()).isEqualTo(u.getId());
        assertThat(p1.getSuspended()).isEqualTo(true);
        log.info("{}", p1);

        uomRepository.unsuspend(p1.getId());
        Uom p2 = uomRepository.findById(p1.getId());
        assertThat(p2.getId()).isEqualTo(p1.getId());
        assertThat(p2.getSuspended()).isEqualTo(false);
        log.info("{}", p2);
    }
}