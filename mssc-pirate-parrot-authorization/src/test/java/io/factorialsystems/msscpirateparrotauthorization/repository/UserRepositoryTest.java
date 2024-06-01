package io.factorialsystems.msscpirateparrotauthorization.repository;

import com.github.pagehelper.Page;
import io.factorialsystems.msscpirateparrotauthorization.model.ApplicationUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@Slf4j
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void findById() {
        final String id = "70c53a68-0df6-11ef-87be-09facd12f38d";
        ApplicationUser applicationUser = userRepository.findById(id);
        log.info("User {}", applicationUser);
    }

    @Test
    void findByUserName() {
        ApplicationUser applicationUser = userRepository.findByUserName("adebola");
        log.info("Loaded User {}", applicationUser);
    }

    @Test
    void findAll() {
        Page<ApplicationUser> applicationUsers = userRepository.findAll();
    }
}