package htw.webtech.foodtracker.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService service;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("test@example.com", "geheim123");
        testUser.setId(1L);
    }

    @Test
    void shouldRegisterUser() throws Exception {
        when(service.register("test@example.com", "geheim123")).thenReturn(testUser);

        String requestBody = """
                {"email": "test@example.com", "password": "geheim123"}
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void shouldRejectRegistrationWithExistingEmail() throws Exception {
        when(service.register(eq("test@example.com"), eq("geheim123")))
                .thenThrow(new RuntimeException("Email already registered"));

        String requestBody = """
                {"email": "test@example.com", "password": "geheim123"}
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void shouldLoginWithCorrectCredentials() throws Exception {
        when(service.login("test@example.com", "geheim123")).thenReturn(testUser);

        String requestBody = """
                {"email": "test@example.com", "password": "geheim123"}
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void shouldRejectLoginWithWrongPassword() throws Exception {
        when(service.login("test@example.com", "falsch"))
                .thenThrow(new RuntimeException("Invalid password"));

        String requestBody = """
                {"email": "test@example.com", "password": "falsch"}
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is5xxServerError());
    }
}