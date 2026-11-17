package dev.portfolio.authguard.security;
import org.junit.jupiter.api.Test;
import java.util.Base64;
import static org.junit.jupiter.api.Assertions.*;
class JwtConfigurationTest {
 private final JwtConfiguration config=new JwtConfiguration();
 @Test void shortKeyIsRejected(){
  String invalid=Base64.getEncoder().encodeToString("short".getBytes());
  assertThrows(IllegalStateException.class,()->config.jwtSecretKey(invalid));
 }
 @Test void valid32ByteKeyIsAccepted(){
  String key=Base64.getEncoder().encodeToString(new byte[32]);
  assertNotNull(config.jwtSecretKey(key));
 }
}
