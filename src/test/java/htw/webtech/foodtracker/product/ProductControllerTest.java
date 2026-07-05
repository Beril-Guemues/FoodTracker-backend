package htw.webtech.foodtracker.product;

import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper objectMapper;

    @MockitoBean
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

    @Test
    void shouldGetAllProducts() throws Exception {
        when(service.getAllProducts()).thenReturn(List.of(testProduct));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Apfel"))
                .andExpect(jsonPath("$[0].calories").value(52));
    }

    @Test
    void shouldSearchProducts() throws Exception {
        when(service.searchProducts("apfel")).thenReturn(List.of(testProduct));

        mockMvc.perform(get("/products/search")
                        .param("q", "apfel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Apfel"));
    }

    @Test
    void shouldGetProductById() throws Exception {
        when(service.getProductById(1L)).thenReturn(testProduct);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Apfel"));
    }

    @Test
    void shouldCreateProduct() throws Exception {
        when(service.saveProduct(any(Product.class))).thenReturn(testProduct);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Apfel"));
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        when(service.updateProduct(eq(1L), any(Product.class))).thenReturn(testProduct);

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        doNothing().when(service).deleteProduct(1L);

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteProduct(1L);
    }
}