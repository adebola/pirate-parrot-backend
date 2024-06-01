package io.factorialsystems.msscpirateparrotgateway.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class UserInfoController {
    @GetMapping("/me")
    public Mono<UserDto> userInfo(@AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            List<String> list = principal.getIdToken().getClaimAsStringList("authorities");
            String authorities = String.join(",", list);
            return Mono.just(
                    new UserDto(
                            principal.getIdToken().getSubject(),
                            principal.getIdToken().getClaim("organization"),
                            authorities));
        }
        return Mono.just(UserDto.ANONYMOUS);
    }

    public record UserDto(String username, String details, String roles) {
        static final UserDto ANONYMOUS = new UserDto("ANONYMOUS", "Spring Boot Tutorial", "NONE");
    }
}
