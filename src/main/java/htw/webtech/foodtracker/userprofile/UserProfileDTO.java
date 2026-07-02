package htw.webtech.foodtracker.userprofile;

public class UserProfileDTO {
    private Long id;
    private double weight;       // in kg
    private String gender;       // "male" / "female"
    private int age;
    private double height;       // in cm
    private double targetWeight; // in kg
    private double calorieNeed;  // berechnet ✅ NEU
    private double waterNeed;    // berechnet ✅ NEU

    public UserProfileDTO() {}

    public UserProfileDTO(UserProfile profile) {
        this.id = profile.getId();
        this.weight = profile.getWeight();
        this.gender = profile.getGender();
        this.age = profile.getAge();
        this.height = profile.getHeight();
        this.targetWeight = profile.getTargetWeight();
        // Berechnungen direkt im DTO
        this.calorieNeed = calculateCalorieNeed(profile);
        this.waterNeed = calculateWaterNeed(profile);
    }

    // Hilfsmethoden für Berechnungen (kopiert aus Service)
    private double calculateCalorieNeed(UserProfile profile) {
        double bmr;
        if ("male".equalsIgnoreCase(profile.getGender())) {
            bmr = 10 * profile.getWeight() + 6.25 * profile.getHeight() - 5 * profile.getAge() + 5;
        } else {
            bmr = 10 * profile.getWeight() + 6.25 * profile.getHeight() - 5 * profile.getAge() - 161;
        }
        return bmr * 1.2; // sitzende Aktivität
    }

    private double calculateWaterNeed(UserProfile profile) {
        return profile.getWeight() * 0.035; // in Litern
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

    public double getTargetWeight() { return targetWeight; }
    public void setTargetWeight(double targetWeight) { this.targetWeight = targetWeight; }

    public double getCalorieNeed() { return calorieNeed; }
    public void setCalorieNeed(double calorieNeed) { this.calorieNeed = calorieNeed; }

    public double getWaterNeed() { return waterNeed; }
    public void setWaterNeed(double waterNeed) { this.waterNeed = waterNeed; }
}