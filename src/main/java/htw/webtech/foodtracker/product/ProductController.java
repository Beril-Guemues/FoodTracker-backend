package htw.webtech.foodtracker.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService service;

    @GetMapping("/products")
    public List<ProductDTO> getAllProducts() {
        logger.info("GET /products - Alle Produkte werden abgerufen");
        return service.getAllProducts().stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/products/search")
    public List<ProductDTO> searchProducts(@RequestParam(required = false) String q) {
        logger.info("GET /products/search - Suche nach: {}", q);
        return service.searchProducts(q).stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/products/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        logger.info("GET /products/{} - Produkt mit ID wird abgerufen", id);
        Product product = service.getProductById(id);
        return new ProductDTO(product);
    }

    @PostMapping("/products")
    public ProductDTO createProduct(@RequestBody Product product) {
        logger.info("POST /products - Neues Produkt wird erstellt: {}", product);
        Product saved = service.saveProduct(product);
        logger.info("POST /products - Produkt erfolgreich erstellt mit ID: {}", saved.getId());
        return new ProductDTO(saved);
    }

    @PutMapping("/products/{id}")
    public ProductDTO updateProduct(@PathVariable Long id, @RequestBody Product product) {
        logger.info("PUT /products/{} - Produkt wird aktualisiert: {}", id, product);
        Product updated = service.updateProduct(id, product);
        logger.info("PUT /products/{} - Produkt erfolgreich aktualisiert", id);
        return new ProductDTO(updated);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        logger.info("DELETE /products/{} - Produkt wird gelöscht", id);
        service.deleteProduct(id);
        logger.info("DELETE /products/{} - Produkt erfolgreich gelöscht", id);
    }
}