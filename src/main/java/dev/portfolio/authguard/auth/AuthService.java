package dev.portfolio.authguard.auth;
import dev.portfolio.authguard.common.DuplicateEmailException;
import dev.portfolio.authguard.user.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class AuthService {
 private final UserRepository users;
 private final PasswordEncoder passwords;
 private final JwtEncoder jwtEncoder;
 private final String issuer;
 private final long ttlMinutes;
 public AuthService(UserRepository users,PasswordEncoder passwords,JwtEncoder jwtEncoder,
                    @Value("${app.jwt.issuer}") String issuer,
                    @Value("${app.jwt.ttl-minutes}") long ttlMinutes) {
  this.users=users; this.passwords=passwords; this.jwtEncoder=jwtEncoder;
  this.issuer=issuer; this.ttlMinutes=ttlMinutes;
 }
 @Transactional
 public UserResponse register(RegisterRequest request) {
  String email=request.email().strip().toLowerCase(Locale.ROOT);
  if (users.existsByEmail(email)) throw new DuplicateEmailException();
  AppUser user=new AppUser(request.name().strip(),email,passwords.encode(request.password()));
  return UserResponse.from(users.saveAndFlush(user));
 }
 @Transactional(readOnly=true)
 public TokenResponse login(LoginRequest request) {
  String email=request.email().strip().toLowerCase(Locale.ROOT);
  AppUser user=users.findByEmail(email).orElseThrow(()->new BadCredentialsException("Credenciais inválidas"));
  if (!passwords.matches(request.password(),user.getPasswordHash()))
   throw new BadCredentialsException("Credenciais inválidas");
  Instant now=Instant.now();
  JwtClaimsSet claims=JwtClaimsSet.builder().issuer(issuer).subject(user.getId().toString())
   .issuedAt(now).expiresAt(now.plus(ttlMinutes,ChronoUnit.MINUTES))
   .claim("roles",List.of(user.getRole().name())).build();
  String jwt=jwtEncoder.encode(JwtEncoderParameters.from(
   JwsHeader.with(MacAlgorithm.HS256).build(),claims)).getTokenValue();
  return new TokenResponse(jwt,"Bearer",ttlMinutes*60);
 }
}
