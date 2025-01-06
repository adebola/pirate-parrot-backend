package io.factorialsystems.msscpirateparrotauthorization.repository;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.factorialsystems.msscpirateparrotauthorization.model.ApplicationUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;

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
        Optional<ApplicationUser> applicationUser = userRepository.findById(id);
        log.info("User {}", applicationUser.get());
    }

    @Test
    void findByUserName() {
        Optional<ApplicationUser> applicationUser = userRepository.findByUserName("adebola");
        log.info("Loaded User {}", applicationUser.get());
    }

    @Test
    void findAll() {
        PageHelper.startPage(1, 20);
        Page<ApplicationUser> users = userRepository.findAll();
        log.info(users.getFirst().toString());
    }
}