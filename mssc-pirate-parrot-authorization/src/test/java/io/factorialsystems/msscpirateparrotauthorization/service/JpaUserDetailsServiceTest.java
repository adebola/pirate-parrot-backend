package io.factorialsystems.msscpirateparrotauthorization.service;

import io.factorialsystems.msscpirateparrotauthorization.dto.ApplicationUserDTO;
import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

@CommonsLog
@SpringBootTest
class JpaUserDetailsServiceTest {

    @Autowired
    private JpaUserDetailsService userDetailsService;

    @Test
    void loadUserByUsername() {
        final UserDetails adebola = userDetailsService.loadUserByUsername("adebola");
        log.info(adebola);
    }

    @Test
    void createUser_NoRoles_Exception() {
        Exception exception = assertThrows(DuplicateKeyException.class, () -> {
            ApplicationUserDTO applicationUserDto = new ApplicationUserDTO();
            applicationUserDto.setUserName("damola");
            applicationUserDto.setFirstName("Adedamola");
            applicationUserDto.setLastName("Omoboya");
            applicationUserDto.setEmail("damola@omoboya.com");
            applicationUserDto.setPassword("password");

            userDetailsService.createUser(applicationUserDto);
        });
    }

    @Test
    @Transactional
    @Rollback
    void createUser_Roles() {
        Set<String> roles = Set.of("User");

        ApplicationUserDTO applicationUserDto = new ApplicationUserDTO();
        applicationUserDto.setUserName("oyinda");
        applicationUserDto.setFirstName("Oyindamola");
        applicationUserDto.setLastName("Omoboya");
        applicationUserDto.setEmail("oyindamola@omoboya.com");
        applicationUserDto.setPassword("password");
        applicationUserDto.setRoles(roles);

        userDetailsService.createUser(applicationUserDto);
    }
}