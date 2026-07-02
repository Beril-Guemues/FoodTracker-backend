package htw.webtech.foodtracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class GoalController {

    @Autowired
    private GoalService service;

    @GetMapping("/goals")
    public List<Goal> getAllGoals() {
        return service.getAllGoals();
    }

    @PostMapping("/goals")
    public Goal createGoal(@RequestBody Goal goal) {
        return service.saveGoal(goal);
    }

    @DeleteMapping("/goals/{id}")
    public void deleteGoal(@PathVariable Long id) {
        service.deleteGoal(id);
    }
}