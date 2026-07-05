package htw.webtech.foodtracker.foodentry;

import htw.webtech.foodtracker.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoodEntryController.class)
class FoodEntryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper objectMapper;

    @MockitoBean
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

    @Test
    void shouldGetAllEntries() throws Exception {
        when(service.getAllEntries()).thenReturn(List.of(testEntry));

        mockMvc.perform(get("/foodentries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].amount").value(150))
                .andExpect(jsonPath("$[0].product.name").value("Apfel"));
    }

    @Test
    void shouldGetEntriesByDate() throws Exception {
        when(service.getEntriesByDate(testDate)).thenReturn(List.of(testEntry));

        mockMvc.perform(get("/foodentries/date")
                        .param("date", "2026-07-05"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].product.name").value("Apfel"));
    }

    @Test
    void shouldGetEntryById() throws Exception {
        when(service.getEntryById(1L)).thenReturn(testEntry);

        mockMvc.perform(get("/foodentries/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.amount").value(150))
                .andExpect(jsonPath("$.product.name").value("Apfel"));
    }

    @Test
    void shouldCreateEntry() throws Exception {
        when(service.saveEntry(any(FoodEntry.class))).thenReturn(testEntry);

        mockMvc.perform(post("/foodentries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEntry)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.product.name").value("Apfel"));
    }

    @Test
    void shouldUpdateEntry() throws Exception {
        when(service.updateEntry(eq(1L), any(FoodEntry.class))).thenReturn(testEntry);

        mockMvc.perform(put("/foodentries/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEntry)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldDeleteEntry() throws Exception {
        doNothing().when(service).deleteEntry(1L);

        mockMvc.perform(delete("/foodentries/1"))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteEntry(1L);
    }
}