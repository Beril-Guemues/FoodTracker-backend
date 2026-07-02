package htw.webtech.foodtracker.goal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GoalService {

    @Autowired
    private GoalRepository repository;

    // 1. Alle Ziele
    public List<Goal> getAllGoals() {
        return repository.findAll();
    }

    // 2. Ziel nach ID
    public Goal getGoalById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found with id: " + id));
    }

    // 3. Ziele nach UserProfile
    public List<Goal> getGoalsByUserProfileId(Long userProfileId) {
        return repository.findByUserProfileId(userProfileId);
    }

    // 4. Ziel speichern (mit Validierung)
    public Goal saveGoal(Goal goal) {
        validateGoal(goal);
        return repository.save(goal);
    }

    // 5. Ziel aktualisieren
    public Goal updateGoal(Long id, Goal goalDetails) {
        Goal existing = getGoalById(id);

        existing.setType(goalDetails.getType());
        existing.setUserProfile(goalDetails.getUserProfile());

        validateGoal(existing);
        return repository.save(existing);
    }

    // 6. Ziel löschen
    public void deleteGoal(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Goal not found with id: " + id);
        }
        repository.deleteById(id);
    }

    // 7. Validierung
    private void validateGoal(Goal goal) {
        if (goal.getType() == null || goal.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Goal type cannot be empty");
        }

        // Erlaubte Goal-Types prüfen
        String type = goal.getType().toLowerCase();
        if (!type.equals("abnehmen") && !type.equals("zunehmen") &&
                !type.equals("muskeln_aufbauen") && !type.equals("gesund_ernaehren")) {
            throw new IllegalArgumentException("Invalid goal type. Allowed: abnehmen, zunehmen, muskeln_aufbauen, gesund_ernaehren");
        }

        if (goal.getUserProfile() == null) {
            throw new IllegalArgumentException("UserProfile cannot be null");
        }
    }
}