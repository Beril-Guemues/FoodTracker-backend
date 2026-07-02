package htw.webtech.foodtracker.goal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class GoalController {

    @Autowired
    private GoalService service;

    // 1. ALLE Ziele (mit DTO)
    @GetMapping("/goals")
    public List<GoalDTO> getAllGoals() {
        return service.getAllGoals().stream()
                .map(GoalDTO::new)
                .collect(Collectors.toList());
    }

    // 2. Ziele nach UserProfile ID (mit DTO) ✅ NEU
    @GetMapping("/goals/user/{userId}")
    public List<GoalDTO> getGoalsByUserProfileId(@PathVariable Long userId) {
        return service.getGoalsByUserProfileId(userId).stream()
                .map(GoalDTO::new)
                .collect(Collectors.toList());
    }

    // 3. Ziel nach ID (mit DTO) ✅ NEU
    @GetMapping("/goals/{id}")
    public GoalDTO getGoalById(@PathVariable Long id) {
        Goal goal = service.getGoalById(id);
        return new GoalDTO(goal);
    }

    // 4. Ziel erstellen (mit DTO)
    @PostMapping("/goals")
    public GoalDTO createGoal(@RequestBody Goal goal) {
        Goal saved = service.saveGoal(goal);
        return new GoalDTO(saved);
    }

    // 5. Ziel aktualisieren (mit DTO) ✅ NEU
    @PutMapping("/goals/{id}")
    public GoalDTO updateGoal(@PathVariable Long id, @RequestBody Goal goal) {
        Goal updated = service.updateGoal(id, goal);
        return new GoalDTO(updated);
    }

    // 6. Ziel löschen
    @DeleteMapping("/goals/{id}")
    public void deleteGoal(@PathVariable Long id) {
        service.deleteGoal(id);
    }
}