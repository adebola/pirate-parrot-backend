package io.factorialsystems.msscpirateparrotauthorization.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.factorialsystems.msscpirateparrotauthorization.dto.ApplicationUserDTO;
import io.factorialsystems.msscpirateparrotauthorization.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotauthorization.dto.RegisterUserDTO;
import io.factorialsystems.msscpirateparrotauthorization.exception.UserExistsException;
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

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final ApplicationUserMapper applicationUserMapper;

    public static final String DEFAULT_USER_ROLE = "USER";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<ApplicationUser> applicationUser = userRepository.findByUserName(username);

        if (applicationUser.isEmpty()) {
            final String errorMessage = String.format("User %s not found", username);
            log.error(errorMessage);
            throw new UsernameNotFoundException(errorMessage);
        }

        return applicationUser.get().toUserDetails();
    }

    public ApplicationUserDTO loadUserById(String id) {
        Optional<ApplicationUser> applicationUser = userRepository.findById(id);

        if (applicationUser.isEmpty()) {
            final String errorMessage = String.format("User %s not found", id);
            log.error(errorMessage);
            throw new UsernameNotFoundException(errorMessage);
        }

        return applicationUserMapper.toDtoFat(applicationUser.get());
    }

    public PagedDTO<ApplicationUserDTO> loadUsers(Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        Page<ApplicationUser> users = userRepository.findAll();
        return createDTO(users);
    }

    public void createUser(RegisterUserDTO registerUserDTO) {

        if (userRepository.findByUserName(registerUserDTO.getUserName()).isPresent()) {
            log.error("User {} already exists", registerUserDTO.getUserName());
            throw new UserExistsException(String.format("User %s already exists", registerUserDTO.getUserName()));
        }

        if (!registerUserDTO.getPassword().equals(registerUserDTO.getMatchingPassword())) {
            log.error("Passwords do not match for user {}", registerUserDTO.getUserName());
            throw new IllegalArgumentException("Passwords do not match");
        }

        Optional<UserAuthority> userAuthority = authorityRepository.findByAuthority(DEFAULT_USER_ROLE);

        if (userAuthority.isEmpty()) {
            log.error("Default user role {} not found", DEFAULT_USER_ROLE);
            throw new IllegalArgumentException(String.format("Default user role %s not found", DEFAULT_USER_ROLE));
        }

        ApplicationUser applicationUser = ApplicationUser.create(
                registerUserDTO.getUserName(),
                registerUserDTO.getFirstName(),
                registerUserDTO.getLastName(),
                registerUserDTO.getEmail(),
                passwordEncoder.encode(registerUserDTO.getPassword()),
                Set.of(userAuthority.get())
        );

        userRepository.save(applicationUser);
        log.info("User created successfully {}", applicationUser);
    }

    public void updateUser(String id, ApplicationUserDTO applicationUserDto) {
        Optional<ApplicationUser> user = userRepository.findById(id);

        if (user.isEmpty()) {
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

    private PagedDTO<ApplicationUserDTO> createDTO(Page<ApplicationUser> users) {
        PagedDTO<ApplicationUserDTO> page = new PagedDTO<>();
        page.setTotalSize((int) users.getTotal());

        page.setPageNumber(users.getPageNum());
        page.setPageSize(users.getPageSize());
        page.setPages(users.getPages());
        page.setList(
                users.getResult().stream()
                        .map(applicationUserMapper::toDtoSlim)
                        .toList()
        );

        return page;
    }
}
