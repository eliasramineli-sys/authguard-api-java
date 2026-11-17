package dev.portfolio.authguard.auth;
import dev.portfolio.authguard.common.DuplicateEmailException;
import dev.portfolio.authguard.user.*;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
class AuthServiceTest {
 private final UserRepository users=mock(UserRepository.class);
 private final JwtEncoder encoder=mock(JwtEncoder.class);
 private final BCryptPasswordEncoder passwords=new BCryptPasswordEncoder();
 private final AuthService service=new AuthService(users,passwords,encoder,"authguard-api",30);
 @Test void registrationUsesNormalizedEmailAndHashedPassword(){
  when(users.saveAndFlush(any(AppUser.class))).thenAnswer(i->i.getArgument(0));
  UserResponse result=service.register(new RegisterRequest(" Ana "," ANA@EXAMPLE.COM ","password-super-safe"));
  assertEquals("ana@example.com",result.email());
  var capture=org.mockito.ArgumentCaptor.forClass(AppUser.class);
  verify(users).saveAndFlush(capture.capture());
  assertTrue(passwords.matches("password-super-safe",capture.getValue().getPasswordHash()));
  assertEquals(Role.USER,capture.getValue().getRole());
 }
 @Test void duplicateEmailIsRejected(){
  when(users.existsByEmail("ana@example.com")).thenReturn(true);
  assertThrows(DuplicateEmailException.class,()->service.register(new RegisterRequest("Ana","ANA@EXAMPLE.COM","password-super-safe")));
 }
 @Test void incorrectPasswordIsRejected(){
  when(users.findByEmail("ana@example.com"))
   .thenReturn(Optional.of(new AppUser("Ana","ana@example.com",passwords.encode("correct-password"))));
  assertThrows(BadCredentialsException.class,()->service.login(new LoginRequest("ana@example.com","wrong-password")));
 }
 @Test void unknownEmailIsRejected(){
  assertThrows(BadCredentialsException.class,()->service.login(new LoginRequest("unknown@example.com","anything")));
 }
}
