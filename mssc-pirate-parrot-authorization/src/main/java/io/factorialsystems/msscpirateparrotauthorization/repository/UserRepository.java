package io.factorialsystems.msscpirateparrotauthorization.repository;

import com.github.pagehelper.Page;
import io.factorialsystems.msscpirateparrotauthorization.model.ApplicationUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserRepository {
    void save(ApplicationUser applicationUser);
    void update(ApplicationUser applicationUser);
    Optional<ApplicationUser> findById(String id);
    Optional<ApplicationUser> findByUserName(String userName);
    Page<ApplicationUser> findAll();
    Page<ApplicationUser> search(String search);
}
