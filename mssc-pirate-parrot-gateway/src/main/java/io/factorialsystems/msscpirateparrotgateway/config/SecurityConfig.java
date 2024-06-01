package io.factorialsystems.msscpirateparrotgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.csrf.XorServerCsrfTokenRequestAttributeHandler;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfig {
//    @Bean
//    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        return http
//                .authorizeExchange(exchange -> exchange
//                        .pathMatchers("/**").permitAll()
//                        .anyExchange().authenticated())
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .oauth2Login(Customizer.withDefaults())
//                .oauth2Client(Customizer.withDefaults())
//                .build();
//    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http.oauth2Login(Customizer.withDefaults());

        http.csrf(
                csrf -> csrf
                        .csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new XorServerCsrfTokenRequestAttributeHandler()::handle)
        );
        http.authorizeExchange(
                exchange -> exchange
                        .anyExchange().authenticated()
        );
        return http.build();
    }

    @Bean
    WebFilter csrfCookieWebFilter() {
        return (exchange, chain) -> {
            Mono<CsrfToken> csrfTokenMono = exchange.getAttributeOrDefault(CsrfToken.class.getName(), Mono.empty());
            return csrfTokenMono.doOnSuccess(csrfToken -> {
                System.out.println(csrfToken.getToken());
            }).then(chain.filter(exchange));
        };
    }
}
