package io.factorialsystems.msscpirateparrotauthorization.service;

import io.factorialsystems.msscpirateparrotauthorization.dto.ApplicationUserDTO;
import io.factorialsystems.msscpirateparrotauthorization.mapper.ApplicationUserMapper;
import io.factorialsystems.msscpirateparrotauthorization.model.ApplicationUser;
import io.factorialsystems.msscpirateparrotauthorization.model.UserAuthority;
import io.factorialsystems.msscpirateparrotauthorization.repository.AuthorityRepository;
import io.factorialsystems.msscpirateparrotauthorization.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final ApplicationUserMapper applicationUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = userRepository.findByUserName(username);

        if (applicationUser == null) {
            final String errorMessage = String.format("User %s not found", username);
            log.error(errorMessage);
            throw new UsernameNotFoundException(errorMessage);
        }

        return applicationUser.toUserDetails();
    }

    public void createUser(ApplicationUserDTO applicationUserDto) {
        final Set<String> roles = applicationUserDto.getRoles();
        final Set<UserAuthority> authorities;

        if (roles == null || roles.isEmpty()) {
            authorities = Set.of();
        } else {
            authorities = new HashSet<>(authorityRepository.findAuthorities(new ArrayList<>(roles)));
        }

        ApplicationUser applicationUser = ApplicationUser.create(
                applicationUserDto.getUserName(),
                applicationUserDto.getFirstName(),
                applicationUserDto.getLastName(),
                applicationUserDto.getEmail(),
                passwordEncoder.encode(applicationUserDto.getPassword()),
                authorities
        );

        userRepository.save(applicationUser);
    }

    public void updateUser(String id, ApplicationUserDTO applicationUserDto) {
        ApplicationUser user = userRepository.findById(id);

        if (user == null) {
            log.error("User {} not found for submitted applicationUserDto {}", id, applicationUserDto);
            throw new UsernameNotFoundException(String.format("User %s not found", id));
        }

        ApplicationUser applicationUser = applicationUserMapper.toEntity(applicationUserDto);
        applicationUser.setId(id);

        log.info("User updated successfully {}", applicationUser);
        userRepository.update(applicationUser);
    }

    public void addRoles(Set<String> roles) {

    }

    public void removeRoles(Set<String> roles) {

    }

    public void changePassword(String oldPassword, String newPassword) {

    }
}
