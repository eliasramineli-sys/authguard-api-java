package dev.portfolio.authguard.security;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
@Configuration
public class JwtConfiguration {
 @Bean
 SecretKey jwtSecretKey(@Value("${app.jwt.secret-base64}") String base64) {
  byte[] decoded;
  try { decoded=Base64.getDecoder().decode(base64); }
  catch (IllegalArgumentException e) { throw new IllegalStateException("JWT_SECRET_BASE64 inválido",e); }
  if (decoded.length<32) throw new IllegalStateException("JWT_SECRET_BASE64 deve representar pelo menos 32 bytes");
  return new SecretKeySpec(decoded,"HmacSHA256");
 }
 @Bean JwtEncoder jwtEncoder(SecretKey key) {
  return new NimbusJwtEncoder(new com.nimbusds.jose.jwk.source.ImmutableSecret<>(key));
 }
 @Bean JwtDecoder jwtDecoder(SecretKey key,@Value("${app.jwt.issuer}") String issuer) {
  NimbusJwtDecoder decoder=NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
  decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuer));
  return decoder;
 }
}
