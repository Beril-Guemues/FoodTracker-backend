package htw.webtech.foodtracker.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
@Slf4j
public class UserController {

    @Autowired
    private UserService service;

    // 1. REGISTRIERUNG
    @PostMapping("/register")
    public User register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        log.info("POST /auth/register - Registrierung für: {}", email);

        User user = service.register(email, password);

        log.info("POST /auth/register - Benutzer erfolgreich registriert mit ID: {}", user.getId());
        return user;
    }

    // 2. LOGIN
    @PostMapping("/login")
    public User login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        log.info("POST /auth/login - Login-Versuch für: {}", email);

        User user = service.login(email, password);

        log.info("POST /auth/login - Benutzer erfolgreich eingeloggt: {}", email);
        return user;
    }
}