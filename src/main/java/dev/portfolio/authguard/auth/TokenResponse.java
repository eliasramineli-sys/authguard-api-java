package dev.portfolio.authguard.auth;
public record TokenResponse(String accessToken,String tokenType,long expiresInSeconds) { }
