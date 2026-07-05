package htw.webtech.foodtracker.userprofile;

public class UserProfileDTO {
    private Long id;
    private double weight;
    private String gender;
    private int age;
    private double height;
    private double calorieNeed;
    private double waterNeed;

    public UserProfileDTO() {}

    public UserProfileDTO(UserProfile profile) {
        this.id = profile.getId();
        this.weight = profile.getWeight();
        this.gender = profile.getGender();
        this.age = profile.getAge();
        this.height = profile.getHeight();
        this.calorieNeed = calculateCalorieNeed(profile);
        this.waterNeed = calculateWaterNeed(profile);
    }

    private double calculateCalorieNeed(UserProfile profile) {
        double bmr;
        if ("male".equalsIgnoreCase(profile.getGender())) {
            bmr = 10 * profile.getWeight() + 6.25 * profile.getHeight() - 5 * profile.getAge() + 5;
        } else {
            bmr = 10 * profile.getWeight() + 6.25 * profile.getHeight() - 5 * profile.getAge() - 161;
        }
        return bmr * 1.2;
    }

    private double calculateWaterNeed(UserProfile profile) {
        return profile.getWeight() * 0.035;
    }

    // Getter und Setter
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


    public double getCalorieNeed() { return calorieNeed; }
    public void setCalorieNeed(double calorieNeed) { this.calorieNeed = calorieNeed; }

    public double getWaterNeed() { return waterNeed; }
    public void setWaterNeed(double waterNeed) { this.waterNeed = waterNeed; }
}