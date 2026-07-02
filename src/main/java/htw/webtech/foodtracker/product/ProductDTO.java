package htw.webtech.foodtracker.product;

public class ProductDTO {
    private Long id;
    private String name;
    private int calories;
    private double protein;
    private double carbs;

    // Leerer Konstruktor (für JSON Serialisierung)
    public ProductDTO() {}

    // Konstruktor aus Entity
    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.calories = product.getCalories();
        this.protein = product.getProtein();
        this.carbs = product.getCarbs();
    }

    // Getter und Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }

    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }

    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { this.carbs = carbs; }
}