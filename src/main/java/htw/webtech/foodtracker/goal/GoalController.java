package htw.webtech.foodtracker.goal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class GoalController {

    private static final Logger logger = LoggerFactory.getLogger(GoalController.class);

    @Autowired
    private GoalService service;

    // 1. ALLE Ziele (mit DTO)
    @GetMapping("/goals")
    public List<GoalDTO> getAllGoals() {
        logger.info("GET /goals - Alle Ziele werden abgerufen");
        return service.getAllGoals().stream()
                .map(GoalDTO::new)
                .collect(Collectors.toList());
    }

    // 2. Ziele nach UserProfile ID (mit DTO)
    @GetMapping("/goals/user/{userId}")
    public List<GoalDTO> getGoalsByUserProfileId(@PathVariable Long userId) {
        logger.info("GET /goals/user/{} - Ziele für User-Profil ID werden abgerufen", userId);
        return service.getGoalsByUserProfileId(userId).stream()
                .map(GoalDTO::new)
                .collect(Collectors.toList());
    }

    // 3. Ziel nach ID (mit DTO)
    @GetMapping("/goals/{id}")
    public GoalDTO getGoalById(@PathVariable Long id) {
        logger.info("GET /goals/{} - Ziel mit ID wird abgerufen", id);
        Goal goal = service.getGoalById(id);
        return new GoalDTO(goal);
    }

    // 4. Ziel erstellen (mit DTO)
    @PostMapping("/goals")
    public GoalDTO createGoal(@RequestBody Goal goal) {
        logger.info("POST /goals - Neues Ziel wird erstellt: {}", goal);
        Goal saved = service.saveGoal(goal);
        logger.info("POST /goals - Ziel erfolgreich erstellt mit ID: {}", saved.getId());
        return new GoalDTO(saved);
    }

    // 5. Ziel aktualisieren (mit DTO)
    @PutMapping("/goals/{id}")
    public GoalDTO updateGoal(@PathVariable Long id, @RequestBody Goal goal) {
        logger.info("PUT /goals/{} - Ziel wird aktualisiert: {}", id, goal);
        Goal updated = service.updateGoal(id, goal);
        logger.info("PUT /goals/{} - Ziel erfolgreich aktualisiert", id);
        return new GoalDTO(updated);
    }

    // 6. Ziel löschen
    @DeleteMapping("/goals/{id}")
    public void deleteGoal(@PathVariable Long id) {
        logger.info("DELETE /goals/{} - Ziel wird gelöscht", id);
        service.deleteGoal(id);
        logger.info("DELETE /goals/{} - Ziel erfolgreich gelöscht", id);
    }
}