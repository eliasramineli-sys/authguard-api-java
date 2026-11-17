package dev.portfolio.authguard.user;
import java.util.NoSuchElementException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/users")
public class UserController {
 private final UserRepository users;
 public UserController(UserRepository users){this.users=users;}
 @GetMapping("/me")
 public UserResponse me(@AuthenticationPrincipal Jwt token){
  Long id=Long.valueOf(token.getSubject());
  return UserResponse.from(users.findById(id).orElseThrow(()->new NoSuchElementException("Usuário não encontrado")));
 }
}
