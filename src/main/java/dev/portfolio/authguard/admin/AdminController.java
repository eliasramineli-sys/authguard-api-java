package dev.portfolio.authguard.admin;
import dev.portfolio.authguard.user.*;
import java.util.List;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
 private final UserRepository users;
 public AdminController(UserRepository users) { this.users=users; }
 @GetMapping("/users")
 public List<UserResponse> users() { return users.findAll().stream().map(UserResponse::from).toList(); }
}
