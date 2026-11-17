package dev.portfolio.authguard.security;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.*;
import org.springframework.security.web.SecurityFilterChain;
@Configuration @EnableMethodSecurity
public class SecurityConfiguration {
 @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
 @Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
  JwtGrantedAuthoritiesConverter authorities=new JwtGrantedAuthoritiesConverter();
  authorities.setAuthoritiesClaimName("roles");
  authorities.setAuthorityPrefix("ROLE_");
  JwtAuthenticationConverter converter=new JwtAuthenticationConverter();
  converter.setJwtGrantedAuthoritiesConverter(authorities);
  http.csrf(csrf->csrf.disable())
   .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
   .authorizeHttpRequests(auth->auth
    .requestMatchers(HttpMethod.POST,"/api/v1/auth/register","/api/v1/auth/login").permitAll()
    .requestMatchers("/actuator/health").permitAll()
    .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
    .anyRequest().authenticated())
   .oauth2ResourceServer(oauth->oauth.jwt(jwt->jwt.jwtAuthenticationConverter(converter)));
  return http.build();
 }
}
