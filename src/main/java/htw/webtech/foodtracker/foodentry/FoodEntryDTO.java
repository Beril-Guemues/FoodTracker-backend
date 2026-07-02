package htw.webtech.foodtracker.foodentry;

import htw.webtech.foodtracker.product.ProductDTO;
import java.time.LocalDate;

public class FoodEntryDTO {
    private Long id;
    private ProductDTO product;
    private double amount;
    private LocalDate date;

    public FoodEntryDTO() {}

    public FoodEntryDTO(FoodEntry entry) {
        this.id = entry.getId();
        this.product = new ProductDTO(entry.getProduct());
        this.amount = entry.getAmount();
        this.date = entry.getDate();
    }

    // Getter und Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ProductDTO getProduct() { return product; }
    public void setProduct(ProductDTO product) { this.product = product; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
