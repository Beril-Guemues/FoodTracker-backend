package htw.webtech.foodtracker.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController                    // ← Das ist ein Annotation, KEIN Interface!
@RequestMapping("/auth")           // ← Basis-Pfad für alle Endpunkte
@CrossOrigin(origins = "*")        // ← CORS erlauben
public class UserController {      // ← Das ist eine KLASSE!

    @Autowired
    private UserService service;   // ← Service wird injected

    @PostMapping("/register")      // ← POST /auth/register
    public User register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        return service.register(email, password);
    }

    @PostMapping("/login")         // ← POST /auth/login
    public User login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        return service.login(email, password);
    }
}