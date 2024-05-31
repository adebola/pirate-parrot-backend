package io.factorialsystems.msscpirateparrotproduct.repository;

import com.github.pagehelper.Page;
import io.factorialsystems.msscpirateparrotproduct.model.Uom;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UomRepository {
    Uom findById(String id);
    Uom findByName(String name);
    Page<Uom> findAll();
    void save(Uom uom);
    void update(Uom uom);
    Page<Uom> search(String search);
    int suspend(String id);
    int unsuspend(String id);
}
