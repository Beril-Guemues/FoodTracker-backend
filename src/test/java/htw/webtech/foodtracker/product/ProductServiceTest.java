package htw.webtech.foodtracker.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Apfel");
        testProduct.setCalories(52);
        testProduct.setProtein(0.3);
        testProduct.setCarbs(14);
    }

    // ===== getAllProducts =====

    @Test
    void shouldGetAllProducts() {
        when(repository.findAll()).thenReturn(List.of(testProduct));

        List<Product> result = service.getAllProducts();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Apfel");
        verify(repository, times(1)).findAll();
    }

    // ===== getProductById =====

    @Test
    void shouldGetProductById() {
        when(repository.findById(1L)).thenReturn(Optional.of(testProduct));

        Product result = service.getProductById(1L);

        assertThat(result.getName()).isEqualTo("Apfel");
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowWhenProductByIdNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getProductById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Product not found with id: 99");
    }

    // ===== searchProducts =====

    @Test
    void shouldSearchProductsByQuery() {
        when(repository.findByNameContainingIgnoreCase("apfel")).thenReturn(List.of(testProduct));

        List<Product> result = service.searchProducts("apfel");

        assertThat(result).hasSize(1);
        verify(repository, times(1)).findByNameContainingIgnoreCase("apfel");
        verify(repository, never()).findAll();
    }

    @Test
    void shouldReturnAllProductsWhenSearchQueryIsEmpty() {
        when(repository.findAll()).thenReturn(List.of(testProduct));

        List<Product> result = service.searchProducts("   ");

        assertThat(result).hasSize(1);
        verify(repository, times(1)).findAll();
        verify(repository, never()).findByNameContainingIgnoreCase(anyString());
    }

    @Test
    void shouldReturnAllProductsWhenSearchQueryIsNull() {
        when(repository.findAll()).thenReturn(List.of(testProduct));

        List<Product> result = service.searchProducts(null);

        assertThat(result).hasSize(1);
        verify(repository, times(1)).findAll();
    }

    // ===== saveProduct =====

    @Test
    void shouldSaveValidProduct() {
        when(repository.save(any(Product.class))).thenReturn(testProduct);

        Product result = service.saveProduct(testProduct);

        assertThat(result.getName()).isEqualTo("Apfel");
        verify(repository, times(1)).save(testProduct);
    }

    @Test
    void shouldThrowWhenSavingProductWithEmptyName() {
        testProduct.setName("  ");

        assertThatThrownBy(() -> service.saveProduct(testProduct))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product name cannot be empty");

        verify(repository, never()).save(any(Product.class));
    }

    @Test
    void shouldThrowWhenSavingProductWithNegativeCalories() {
        testProduct.setCalories(-1);

        assertThatThrownBy(() -> service.saveProduct(testProduct))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Calories cannot be negative");

        verify(repository, never()).save(any(Product.class));
    }

    @Test
    void shouldThrowWhenSavingProductWithNegativeProtein() {
        testProduct.setProtein(-0.1);

        assertThatThrownBy(() -> service.saveProduct(testProduct))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Protein cannot be negative");

        verify(repository, never()).save(any(Product.class));
    }

    @Test
    void shouldThrowWhenSavingProductWithNegativeCarbs() {
        testProduct.setCarbs(-5);

        assertThatThrownBy(() -> service.saveProduct(testProduct))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Carbs cannot be negative");

        verify(repository, never()).save(any(Product.class));
    }

    // ===== updateProduct =====

    @Test
    void shouldUpdateExistingProduct() {
        Product updatedDetails = new Product();
        updatedDetails.setName("Birne");
        updatedDetails.setCalories(57);
        updatedDetails.setProtein(0.4);
        updatedDetails.setCarbs(15);

        when(repository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(repository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product result = service.updateProduct(1L, updatedDetails);

        assertThat(result.getName()).isEqualTo("Birne");
        assertThat(result.getCalories()).isEqualTo(57);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentProduct() {
        Product updatedDetails = new Product();
        updatedDetails.setName("Birne");
        updatedDetails.setCalories(57);
        updatedDetails.setProtein(0.4);
        updatedDetails.setCarbs(15);

        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateProduct(99L, updatedDetails))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Product not found with id: 99");

        verify(repository, never()).save(any(Product.class));
    }

    @Test
    void shouldThrowWhenUpdatingProductWithInvalidData() {
        Product updatedDetails = new Product();
        updatedDetails.setName("");
        updatedDetails.setCalories(57);
        updatedDetails.setProtein(0.4);
        updatedDetails.setCarbs(15);

        when(repository.findById(1L)).thenReturn(Optional.of(testProduct));

        assertThatThrownBy(() -> service.updateProduct(1L, updatedDetails))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product name cannot be empty");

        verify(repository, never()).save(any(Product.class));
    }

    // ===== deleteProduct =====

    @Test
    void shouldDeleteExistingProduct() {
        when(repository.existsById(1L)).thenReturn(true);

        service.deleteProduct(1L);

        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentProduct() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteProduct(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Product not found with id: 99");

        verify(repository, never()).deleteById(anyLong());
    }
}