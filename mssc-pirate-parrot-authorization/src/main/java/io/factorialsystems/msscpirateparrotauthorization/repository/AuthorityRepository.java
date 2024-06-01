package io.factorialsystems.msscpirateparrotauthorization.repository;

import com.github.pagehelper.Page;
import io.factorialsystems.msscpirateparrotauthorization.model.UserAuthority;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AuthorityRepository {
    List<UserAuthority> findAuthorities(List<String> item);
    Page<UserAuthority> findAll();
    Optional<UserAuthority> findById(String id);
    Optional<UserAuthority> findByAuthority(String authority);
    void save (UserAuthority userAuthority);
    void update(UserAuthority userAuthority);
}
