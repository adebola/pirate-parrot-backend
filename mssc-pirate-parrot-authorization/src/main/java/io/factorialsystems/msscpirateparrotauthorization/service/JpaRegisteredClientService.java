package io.factorialsystems.msscpirateparrotauthorization.service;

import io.factorialsystems.msscpirateparrotauthorization.config.RedisConfig;
import io.factorialsystems.msscpirateparrotauthorization.model.ApplicationRegisteredClient;
import io.factorialsystems.msscpirateparrotauthorization.repository.ApplicationRegisteredClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
//@Service
@RequiredArgsConstructor
public class JpaRegisteredClientService implements RegisteredClientRepository {
    private final CacheManager cacheManager;
    private final ApplicationRegisteredClientRepository repository;

    private static final String DEFAULT_AUTHENTICATION_METHOD = "client_secret_basic";

    @Override
    public void save(RegisteredClient registeredClient) {
        ApplicationRegisteredClient client = createApplicationRegisteredClient(registeredClient);
        saveRegisteredClient(client);
    }

    @Override
    public RegisteredClient findById(String id) {
        final ApplicationRegisteredClient client = repository.findById(id);
        return createRegisteredClient(client);
    }

    @Override
    @Cacheable(value = RedisConfig.REGISTERED_CLIENT_CACHE_NAME, key = "#clientId", unless = "#result == null")
    public RegisteredClient findByClientId(String clientId) {
        log.info("retrieving client Id {}", clientId);

        final ApplicationRegisteredClient client = repository.findByClientId(clientId);
        if (client == null) return null;
        return createRegisteredClient(client);
    }

    public void saveRegisteredClient(ApplicationRegisteredClient applicationRegisteredClient) {
        repository.save(applicationRegisteredClient);
    }

    private ApplicationRegisteredClient createApplicationRegisteredClient(RegisteredClient registeredClient) {
        final Set<ClientAuthenticationMethod> clientAuthenticationMethods = registeredClient.getClientAuthenticationMethods();

        ApplicationRegisteredClient applicationRegisteredClient = new ApplicationRegisteredClient();
        applicationRegisteredClient.setClientSecret(registeredClient.getClientSecret());
        applicationRegisteredClient.setClientId(registeredClient.getClientId());
        applicationRegisteredClient.setClientAuthenticationMethod(
                clientAuthenticationMethods.isEmpty() ? DEFAULT_AUTHENTICATION_METHOD : clientAuthenticationMethods.toArray()[0].toString()
        );
        applicationRegisteredClient.setScopes(String.join(", ", registeredClient.getScopes()));
        applicationRegisteredClient.setAuthorizationGrantTypes(
                String.join(",", registeredClient.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue).toList())
        );
        applicationRegisteredClient.setRedirectUris(String.join(",", registeredClient.getRedirectUris()));
        applicationRegisteredClient.setPostLogoutRedirectUris(String.join(",", registeredClient.getPostLogoutRedirectUris()));

        // Client Settings
        final ClientSettings clientSettings = registeredClient.getClientSettings();
        if (clientSettings == null) {
            applicationRegisteredClient.setRequireAuthorizationConsent(false);
            applicationRegisteredClient.setRequireProofKey(false);
        } else {
            applicationRegisteredClient.setRequireAuthorizationConsent(registeredClient.getClientSettings().isRequireAuthorizationConsent());
            applicationRegisteredClient.setRequireProofKey(registeredClient.getClientSettings().isRequireProofKey());
        }

        // Token Settings
        final TokenSettings tokenSettings = registeredClient.getTokenSettings();
        if (tokenSettings == null) {
            applicationRegisteredClient.setAccessTokenTTL(86400L);
            applicationRegisteredClient.setAuthorizationCodeTTL(3600L);
            applicationRegisteredClient.setRefreshTokenTTL(3600L);
            applicationRegisteredClient.setReuseRefreshToken(true);
            applicationRegisteredClient.setIdTokenSignatureAlgorithm("RS256");
            applicationRegisteredClient.setOAuth2TokenFormat("self_contained");
        } else {
            applicationRegisteredClient.setAccessTokenTTL(tokenSettings.getAccessTokenTimeToLive().toMillis());
            applicationRegisteredClient.setAuthorizationCodeTTL(tokenSettings.getAuthorizationCodeTimeToLive().toMillis());
            applicationRegisteredClient.setRefreshTokenTTL( tokenSettings.getRefreshTokenTimeToLive().toMillis());
            applicationRegisteredClient.setIdTokenSignatureAlgorithm(tokenSettings.getIdTokenSignatureAlgorithm().getName());
            applicationRegisteredClient.setOAuth2TokenFormat(tokenSettings.getAccessTokenFormat().getValue());
            applicationRegisteredClient.setReuseRefreshToken(tokenSettings.isReuseRefreshTokens());
        }

      return applicationRegisteredClient;
    }

    private RegisteredClient createRegisteredClient(ApplicationRegisteredClient c) {
        final Set<String> clientScopes = StringUtils.commaDelimitedListToSet(c.getScopes());
        final Set<String> redirectUris = StringUtils.commaDelimitedListToSet(c.getRedirectUris());
        final Set<String> postRedirectUris = StringUtils.commaDelimitedListToSet(c.getPostLogoutRedirectUris());
        final Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(c.getAuthorizationGrantTypes());

        return RegisteredClient.withId(c.getId())
                .clientId(c.getClientId())
                .clientSecret(c.getClientSecret())
                .clientAuthenticationMethod(new ClientAuthenticationMethod(c.getClientAuthenticationMethod()))
                .authorizationGrantTypes((grantTypes) ->
                        authorizationGrantTypes.forEach(grantType ->
                                grantTypes.add(ResolveAuthorizationGrantType.resolve(grantType))))
                .redirectUris(uris -> uris.addAll(redirectUris.stream()
                        .map(String::trim)
                        .collect(Collectors.toSet()))
                )
                .postLogoutRedirectUris(uris -> uris.addAll(postRedirectUris.stream()
                        .map(String::trim)
                        .collect(Collectors.toSet()))
                )
                .scopes(scopes -> scopes.addAll(clientScopes.stream()
                        .map(String::trim)
                        .collect(Collectors.toSet()))
                )
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(c.getRequireAuthorizationConsent())
                        .requireProofKey(c.getRequireProofKey())
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .reuseRefreshTokens(c.getReuseRefreshToken())
                        .idTokenSignatureAlgorithm(SignatureAlgorithm.valueOf(c.getIdTokenSignatureAlgorithm()))
                        .accessTokenTimeToLive(Duration.ofMillis(c.getAccessTokenTTL()))
                        .refreshTokenTimeToLive(Duration.ofMillis(c.getRefreshTokenTTL()))
                        .authorizationCodeTimeToLive(Duration.ofMillis(c.getAuthorizationCodeTTL()))
                        .accessTokenFormat(new OAuth2TokenFormat(c.getOAuth2TokenFormat()))
                        .build())
                .build();
    }

    public void modifyRegisteredClient(ApplicationRegisteredClient applicationRegisteredClient) {
        repository.update(applicationRegisteredClient);
        final Cache cache = cacheManager.getCache(RedisConfig.REGISTERED_CLIENT_CACHE_NAME);
        if (cache != null) cache.evict(applicationRegisteredClient.getClientId());
    }
}
