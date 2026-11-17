package dev.portfolio.authguard.auth;
import jakarta.validation.constraints.*;
public record RegisterRequest(
 @NotBlank @Size(min=2,max=100) String name,
 @NotBlank @Email @Size(max=254) String email,
 @NotBlank @Size(min=12,max=72) String password
) { }
