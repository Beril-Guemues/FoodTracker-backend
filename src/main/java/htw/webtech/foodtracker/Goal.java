package htw.webtech.foodtracker;

import jakarta.persistence.*;

@Entity
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // "abnehmen", "zunehmen", "muskeln_aufbauen", "gesund_ernaehren"

    @ManyToOne
    private UserProfile userProfile;

    public Goal() {}

    public Goal(Long id, String type, UserProfile userProfile) {
        this.id = id;
        this.type = type;
        this.userProfile = userProfile;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public UserProfile getUserProfile() { return userProfile; }
    public void setUserProfile(UserProfile userProfile) { this.userProfile = userProfile; }
}