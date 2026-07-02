package htw.webtech.foodtracker;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class FoodEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    private double amount; // z.B. 2 (Stück) oder 150 (Gramm)
    private LocalDate date;

    public FoodEntry() {}

    public FoodEntry(Long id, Product product, double amount, LocalDate date) {
        this.id = id;
        this.product = product;
        this.amount = amount;
        this.date = date;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}