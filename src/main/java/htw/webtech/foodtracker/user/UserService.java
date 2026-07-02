package htw.webtech.foodtracker.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User register(String email, String password) {
        // Prüfen ob User existiert
        if (repository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User(email, password); // ⚠️ Passwort sollte gehasht werden!
        return repository.save(user);
    }

    public User login(String email, String password) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ⚠️ Passwort-Vergleich (später mit BCrypt)
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}