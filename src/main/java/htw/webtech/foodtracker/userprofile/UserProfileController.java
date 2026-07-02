package htw.webtech.foodtracker.userprofile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class UserProfileController {

    @Autowired
    private UserProfileService service;

    // 1. ALLE Profile (mit DTO)
    @GetMapping("/profiles")
    public List<UserProfileDTO> getAllProfiles() {
        return service.getAllProfiles().stream()
                .map(UserProfileDTO::new)
                .collect(Collectors.toList());
    }

    // 2. Profil nach ID (mit DTO)
    @GetMapping("/profiles/{id}")
    public UserProfileDTO getProfile(@PathVariable Long id) {
        UserProfile profile = service.getProfile(id);
        return new UserProfileDTO(profile);
    }

    // 3. Profil erstellen (mit DTO)
    @PostMapping("/profiles")
    public UserProfileDTO createProfile(@RequestBody UserProfile profile) {
        UserProfile saved = service.saveProfile(profile);
        return new UserProfileDTO(saved);
    }

    // 4. Profil aktualisieren (mit DTO) ✅ NEU
    @PutMapping("/profiles/{id}")
    public UserProfileDTO updateProfile(@PathVariable Long id, @RequestBody UserProfile profile) {
        UserProfile updated = service.updateProfile(id, profile);
        return new UserProfileDTO(updated);
    }

    // 5. Profil löschen ✅ NEU
    @DeleteMapping("/profiles/{id}")
    public void deleteProfile(@PathVariable Long id) {
        service.deleteProfile(id);
    }

    // 6. Kalorienbedarf (als einfacher Double)
    @GetMapping("/profiles/{id}/calorie-need")
    public double getCalorieNeed(@PathVariable Long id) {
        UserProfile profile = service.getProfile(id);
        return service.calculateCalorieNeed(profile);
    }

    // 7. Wasserbedarf (als einfacher Double)
    @GetMapping("/profiles/{id}/water-need")
    public double getWaterNeed(@PathVariable Long id) {
        UserProfile profile = service.getProfile(id);
        return service.calculateWaterNeed(profile);
    }
}