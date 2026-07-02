package htw.webtech.foodtracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class UserProfileController {

    @Autowired
    private UserProfileService service;

    @GetMapping("/profiles")
    public List<UserProfile> getAllProfiles() {
        return service.getAllProfiles();
    }

    @GetMapping("/profiles/{id}")
    public UserProfile getProfile(@PathVariable Long id) {
        return service.getProfile(id);
    }

    @PostMapping("/profiles")
    public UserProfile createProfile(@RequestBody UserProfile profile) {
        return service.saveProfile(profile);
    }

    @GetMapping("/profiles/{id}/calorie-need")
    public double getCalorieNeed(@PathVariable Long id) {
        return service.calculateCalorieNeed(service.getProfile(id));
    }

    @GetMapping("/profiles/{id}/water-need")
    public double getWaterNeed(@PathVariable Long id) {
        return service.calculateWaterNeed(service.getProfile(id));
    }
}