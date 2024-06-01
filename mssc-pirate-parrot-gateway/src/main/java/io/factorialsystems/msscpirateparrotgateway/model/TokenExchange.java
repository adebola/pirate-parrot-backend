package io.factorialsystems.msscpirateparrotgateway.model;

public record TokenExchange(String redirect_uri, String grant_type, String code, String client_id, String client_secret) { }
