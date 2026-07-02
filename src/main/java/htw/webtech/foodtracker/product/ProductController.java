package htw.webtech.foodtracker.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService service;

    // 1. ALLE Produkte abrufen (mit DTO)
    @GetMapping("/products")
    public List<ProductDTO> getAllProducts() {
        return service.getAllProducts().stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    // 2. NEU: Produkte SUCHE (mit DTO)
    @GetMapping("/products/search")
    public List<ProductDTO> searchProducts(@RequestParam(required = false) String q) {
        return service.searchProducts(q).stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    // 3. NEU: Produkt nach ID abrufen (mit DTO)
    @GetMapping("/products/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        Product product = service.getProductById(id);
        return new ProductDTO(product);
    }

    // 4. NEU: Produkt erstellen (mit DTO)
    @PostMapping("/products")
    public ProductDTO createProduct(@RequestBody Product product) {
        Product saved = service.saveProduct(product);
        return new ProductDTO(saved);
    }

    // 5. NEU: Produkt löschen
    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
    }

    // 6. NEU: Produkt aktualisieren
    @PutMapping("/products/{id}")
    public ProductDTO updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updated = service.updateProduct(id, product);
        return new ProductDTO(updated);
    }
}