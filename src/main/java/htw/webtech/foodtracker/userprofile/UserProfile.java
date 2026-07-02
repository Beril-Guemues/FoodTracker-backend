package htw.webtech.foodtracker.userprofile;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double weight;       // in kg
    private String gender;       // "male" / "female"
    private int age;
    private double height;       // in cm
    private double targetWeight; // in kg

    public UserProfile() {}

    public UserProfile(Long id, double weight, String gender, int age, double height, double targetWeight) {
        this.id = id;
        this.weight = weight;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.targetWeight = targetWeight;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getTargetWeight() { return targetWeight; }
    public void setTargetWeight(double targetWeight) { this.targetWeight = targetWeight; }
}