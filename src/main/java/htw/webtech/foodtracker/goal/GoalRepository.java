package htw.webtech.foodtracker.goal;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    // ⚠️ NEU: Goals nach UserProfile ID finden
    List<Goal> findByUserProfileId(Long userProfileId);
}