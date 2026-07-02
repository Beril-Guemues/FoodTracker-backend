package htw.webtech.foodtracker.goal;

import htw.webtech.foodtracker.userprofile.UserProfileDTO;

public class GoalDTO {
    private Long id;
    private String type; // "abnehmen", "zunehmen", "muskeln_aufbauen", "gesund_ernaehren"
    private UserProfileDTO userProfile;

    public GoalDTO() {}

    public GoalDTO(Goal goal) {
        this.id = goal.getId();
        this.type = goal.getType();
        if (goal.getUserProfile() != null) {
            this.userProfile = new UserProfileDTO(goal.getUserProfile());
        }
    }

    // Getter und Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public UserProfileDTO getUserProfile() { return userProfile; }
    public void setUserProfile(UserProfileDTO userProfile) { this.userProfile = userProfile; }
}