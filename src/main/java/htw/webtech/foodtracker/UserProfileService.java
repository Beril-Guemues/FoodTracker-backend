package htw.webtech.foodtracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository repository;

    public List<UserProfile> getAllProfiles() {
        return repository.findAll();
    }

    public UserProfile getProfile(Long id) {
        return repository.findById(id).orElse(null);
    }

    public UserProfile saveProfile(UserProfile profile) {
        return repository.save(profile);
    }

    // Mifflin-St Jeor Formel für Grundumsatz (BMR)
    public double calculateCalorieNeed(UserProfile profile) {
        double bmr;
        if ("male".equalsIgnoreCase(profile.getGender())) {
            bmr = 10 * profile.getWeight() + 6.25 * profile.getHeight() - 5 * profile.getAge() + 5;
        } else {
            bmr = 10 * profile.getWeight() + 6.25 * profile.getHeight() - 5 * profile.getAge() - 161;
        }
        return bmr * 1.2; // sitzende Aktivität als Basis-Faktor
    }

    // 35ml pro kg Körpergewicht als grobe Empfehlung
    public double calculateWaterNeed(UserProfile profile) {
        return profile.getWeight() * 0.035; // in Litern
    }
}