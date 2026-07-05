package htw.webtech.foodtracker.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product getProductById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public List<Product> searchProducts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return repository.findAll();
        }
        return repository.findByNameContainingIgnoreCase(query);
    }

    public Product saveProduct(Product product) {
        validateProduct(product);
        return repository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product existing = getProductById(id);
        existing.setName(productDetails.getName());
        existing.setCalories(productDetails.getCalories());
        existing.setProtein(productDetails.getProtein());
        existing.setCarbs(productDetails.getCarbs());
        validateProduct(existing);
        return repository.save(existing);
    }

    public void deleteProduct(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        repository.deleteById(id);
    }

    // getAllRecipes() wurde ENTFERNT!

    private void validateProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (product.getCalories() < 0) {
            throw new IllegalArgumentException("Calories cannot be negative");
        }
        if (product.getProtein() < 0) {
            throw new IllegalArgumentException("Protein cannot be negative");
        }
        if (product.getCarbs() < 0) {
            throw new IllegalArgumentException("Carbs cannot be negative");
        }
    }
}