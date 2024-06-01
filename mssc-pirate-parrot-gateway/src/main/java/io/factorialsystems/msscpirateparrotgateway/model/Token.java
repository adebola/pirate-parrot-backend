package io.factorialsystems.msscpirateparrotgateway.model;

public record Token(String access_token, String refresh_token,
                    String id_token, String scope, Integer expiresIn) {
}
