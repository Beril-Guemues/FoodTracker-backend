package htw.webtech.foodtracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GoalService {

    @Autowired
    private GoalRepository repository;

    public List<Goal> getAllGoals() {
        return repository.findAll();
    }

    public Goal saveGoal(Goal goal) {
        return repository.save(goal);
    }

    public void deleteGoal(Long id) {
        repository.deleteById(id);
    }
}