package htw.webtech.foodtracker.userprofile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository repository;

    // 1. Alle Profile
    public List<UserProfile> getAllProfiles() {
        return repository.findAll();
    }

    // 2. Profil nach ID
    public UserProfile getProfile(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserProfile not found with id: " + id));
    }

    // 3. Profil speichern (mit Validierung)
    public UserProfile saveProfile(UserProfile profile) {
        validateProfile(profile);
        return repository.save(profile);
    }

    // 4. Profil aktualisieren ✅ NEU
    public UserProfile updateProfile(Long id, UserProfile profileDetails) {
        UserProfile existing = getProfile(id);

        existing.setWeight(profileDetails.getWeight());
        existing.setGender(profileDetails.getGender());
        existing.setAge(profileDetails.getAge());
        existing.setHeight(profileDetails.getHeight());
        existing.setTargetWeight(profileDetails.getTargetWeight());

        validateProfile(existing);
        return repository.save(existing);
    }

    // 5. Profil löschen ✅ NEU
    public void deleteProfile(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("UserProfile not found with id: " + id);
        }
        repository.deleteById(id);
    }

    // 6. Kalorienbedarf berechnen
    public double calculateCalorieNeed(UserProfile profile) {
        double bmr;
        if ("male".equalsIgnoreCase(profile.getGender())) {
            bmr = 10 * profile.getWeight() + 6.25 * profile.getHeight() - 5 * profile.getAge() + 5;
        } else {
            bmr = 10 * profile.getWeight() + 6.25 * profile.getHeight() - 5 * profile.getAge() - 161;
        }
        return bmr * 1.2; // sitzende Aktivität
    }

    // 7. Wasserbedarf berechnen
    public double calculateWaterNeed(UserProfile profile) {
        return profile.getWeight() * 0.035; // in Litern
    }

    // 8. Validierung
    private void validateProfile(UserProfile profile) {
        if (profile.getWeight() <= 0) {
            throw new IllegalArgumentException("Weight must be greater than 0");
        }
        if (profile.getGender() == null || profile.getGender().trim().isEmpty()) {
            throw new IllegalArgumentException("Gender cannot be empty");
        }
        if (!profile.getGender().equalsIgnoreCase("male") &&
                !profile.getGender().equalsIgnoreCase("female")) {
            throw new IllegalArgumentException("Gender must be 'male' or 'female'");
        }
        if (profile.getAge() < 1 || profile.getAge() > 120) {
            throw new IllegalArgumentException("Age must be between 1 and 120");
        }
        if (profile.getHeight() <= 0) {
            throw new IllegalArgumentException("Height must be greater than 0");
        }
        if (profile.getTargetWeight() <= 0) {
            throw new IllegalArgumentException("Target weight must be greater than 0");
        }
    }
}