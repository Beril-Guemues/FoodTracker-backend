package htw.webtech.foodtracker.foodentry;

import htw.webtech.foodtracker.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodEntryServiceTest {

    @Mock
    private FoodEntryRepository repository;

    @InjectMocks
    private FoodEntryService service;

    private Product testProduct;
    private FoodEntry testEntry;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Apfel");
        testProduct.setCalories(52);
        testProduct.setProtein(0.3);
        testProduct.setCarbs(14);

        testDate = LocalDate.of(2026, 7, 5);

        testEntry = new FoodEntry();
        testEntry.setId(1L);
        testEntry.setProduct(testProduct);
        testEntry.setAmount(150);
        testEntry.setDate(testDate);
    }

    // ===== getAllEntries =====

    @Test
    void shouldGetAllEntries() {
        when(repository.findAll()).thenReturn(List.of(testEntry));

        List<FoodEntry> result = service.getAllEntries();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProduct().getName()).isEqualTo("Apfel");
        verify(repository, times(1)).findAll();
    }

    // ===== getEntriesByDate =====

    @Test
    void shouldGetEntriesByDate() {
        when(repository.findByDate(testDate)).thenReturn(List.of(testEntry));

        List<FoodEntry> result = service.getEntriesByDate(testDate);

        assertThat(result).hasSize(1);
        verify(repository, times(1)).findByDate(testDate);
    }

    @Test
    void shouldThrowWhenDateIsNull() {
        assertThatThrownBy(() -> service.getEntriesByDate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Date cannot be null");

        verify(repository, never()).findByDate(any());
    }

    // ===== getEntryById =====

    @Test
    void shouldGetEntryById() {
        when(repository.findById(1L)).thenReturn(Optional.of(testEntry));

        FoodEntry result = service.getEntryById(1L);

        assertThat(result.getAmount()).isEqualTo(150);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowWhenEntryByIdNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getEntryById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("FoodEntry not found with id: 99");
    }

    // ===== saveEntry =====

    @Test
    void shouldSaveValidEntry() {
        when(repository.save(any(FoodEntry.class))).thenReturn(testEntry);

        FoodEntry result = service.saveEntry(testEntry);

        assertThat(result.getAmount()).isEqualTo(150);
        verify(repository, times(1)).save(testEntry);
    }

    @Test
    void shouldThrowWhenSavingEntryWithoutProduct() {
        testEntry.setProduct(null);

        assertThatThrownBy(() -> service.saveEntry(testEntry))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product cannot be null");

        verify(repository, never()).save(any(FoodEntry.class));
    }

    @Test
    void shouldThrowWhenSavingEntryWithZeroAmount() {
        testEntry.setAmount(0);

        assertThatThrownBy(() -> service.saveEntry(testEntry))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Amount must be greater than 0");

        verify(repository, never()).save(any(FoodEntry.class));
    }

    @Test
    void shouldThrowWhenSavingEntryWithNegativeAmount() {
        testEntry.setAmount(-10);

        assertThatThrownBy(() -> service.saveEntry(testEntry))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Amount must be greater than 0");

        verify(repository, never()).save(any(FoodEntry.class));
    }

    @Test
    void shouldThrowWhenSavingEntryWithoutDate() {
        testEntry.setDate(null);

        assertThatThrownBy(() -> service.saveEntry(testEntry))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Date cannot be null");

        verify(repository, never()).save(any(FoodEntry.class));
    }

    // ===== updateEntry =====

    @Test
    void shouldUpdateExistingEntry() {
        Product newProduct = new Product();
        newProduct.setId(2L);
        newProduct.setName("Birne");
        newProduct.setCalories(57);
        newProduct.setProtein(0.4);
        newProduct.setCarbs(15);

        FoodEntry updatedDetails = new FoodEntry();
        updatedDetails.setProduct(newProduct);
        updatedDetails.setAmount(200);
        updatedDetails.setDate(testDate);

        when(repository.findById(1L)).thenReturn(Optional.of(testEntry));
        when(repository.save(any(FoodEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FoodEntry result = service.updateEntry(1L, updatedDetails);

        assertThat(result.getProduct().getName()).isEqualTo("Birne");
        assertThat(result.getAmount()).isEqualTo(200);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(FoodEntry.class));
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentEntry() {
        FoodEntry updatedDetails = new FoodEntry();
        updatedDetails.setProduct(testProduct);
        updatedDetails.setAmount(200);
        updatedDetails.setDate(testDate);

        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateEntry(99L, updatedDetails))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("FoodEntry not found with id: 99");

        verify(repository, never()).save(any(FoodEntry.class));
    }

    @Test
    void shouldThrowWhenUpdatingEntryWithInvalidData() {
        FoodEntry updatedDetails = new FoodEntry();
        updatedDetails.setProduct(testProduct);
        updatedDetails.setAmount(-5);
        updatedDetails.setDate(testDate);

        when(repository.findById(1L)).thenReturn(Optional.of(testEntry));

        assertThatThrownBy(() -> service.updateEntry(1L, updatedDetails))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Amount must be greater than 0");

        verify(repository, never()).save(any(FoodEntry.class));
    }

    // ===== deleteEntry =====

    @Test
    void shouldDeleteExistingEntry() {
        when(repository.existsById(1L)).thenReturn(true);

        service.deleteEntry(1L);

        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentEntry() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteEntry(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("FoodEntry not found with id: 99");

        verify(repository, never()).deleteById(anyLong());
    }
}