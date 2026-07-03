package htw.webtech.foodtracker.userprofile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private UserProfileService service;

    @GetMapping("/profiles")
    public List<UserProfileDTO> getAllProfiles() {
        logger.info("GET /profiles - Alle Profile werden abgerufen");
        return service.getAllProfiles().stream()
                .map(UserProfileDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/profiles/{id}")
    public UserProfileDTO getProfile(@PathVariable Long id) {
        logger.info("GET /profiles/{} - Profil mit ID wird abgerufen", id);
        UserProfile profile = service.getProfile(id);
        return new UserProfileDTO(profile);
    }

    @PostMapping("/profiles")
    public UserProfileDTO createProfile(@RequestBody UserProfile profile) {
        logger.info("POST /profiles - Neues Profil wird erstellt: {}", profile);
        UserProfile saved = service.saveProfile(profile);
        logger.info("POST /profiles - Profil erfolgreich erstellt mit ID: {}", saved.getId());
        return new UserProfileDTO(saved);
    }

    @PutMapping("/profiles/{id}")
    public UserProfileDTO updateProfile(@PathVariable Long id, @RequestBody UserProfile profile) {
        logger.info("PUT /profiles/{} - Profil wird aktualisiert: {}", id, profile);
        UserProfile updated = service.updateProfile(id, profile);
        logger.info("PUT /profiles/{} - Profil erfolgreich aktualisiert", id);
        return new UserProfileDTO(updated);
    }

    @DeleteMapping("/profiles/{id}")
    public void deleteProfile(@PathVariable Long id) {
        logger.info("DELETE /profiles/{} - Profil wird gelöscht", id);
        service.deleteProfile(id);
        logger.info("DELETE /profiles/{} - Profil erfolgreich gelöscht", id);
    }

    @GetMapping("/profiles/{id}/calorie-need")
    public double getCalorieNeed(@PathVariable Long id) {
        logger.info("GET /profiles/{}/calorie-need - Kalorienbedarf wird berechnet", id);
        UserProfile profile = service.getProfile(id);
        double result = service.calculateCalorieNeed(profile);
        logger.info("GET /profiles/{}/calorie-need - Kalorienbedarf: {} kcal", id, result);
        return result;
    }

    @GetMapping("/profiles/{id}/water-need")
    public double getWaterNeed(@PathVariable Long id) {
        logger.info("GET /profiles/{}/water-need - Wasserbedarf wird berechnet", id);
        UserProfile profile = service.getProfile(id);
        double result = service.calculateWaterNeed(profile);
        logger.info("GET /profiles/{}/water-need - Wasserbedarf: {} L", id, result);
        return result;
    }
}