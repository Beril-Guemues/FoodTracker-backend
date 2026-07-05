package htw.webtech.foodtracker.userprofile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserProfileController.class)
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper objectMapper;

    @MockitoBean
    private UserProfileService service;

    private UserProfile testProfile;

    @BeforeEach
    void setUp() {
        testProfile = new UserProfile();
        testProfile.setId(1L);
        testProfile.setWeight(70);
        testProfile.setGender("male");
        testProfile.setAge(30);
        testProfile.setHeight(180);
    }

    @Test
    void shouldGetAllProfiles() throws Exception {
        when(service.getAllProfiles()).thenReturn(List.of(testProfile));

        mockMvc.perform(get("/profiles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].gender").value("male"));
    }

    @Test
    void shouldGetProfileById() throws Exception {
        when(service.getProfile(1L)).thenReturn(testProfile);

        mockMvc.perform(get("/profiles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.weight").value(70))
                .andExpect(jsonPath("$.height").value(180));
    }

    @Test
    void shouldCreateProfile() throws Exception {
        when(service.saveProfile(any(UserProfile.class))).thenReturn(testProfile);

        mockMvc.perform(post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProfile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.gender").value("male"));
    }

    @Test
    void shouldUpdateProfile() throws Exception {
        when(service.updateProfile(eq(1L), any(UserProfile.class))).thenReturn(testProfile);

        mockMvc.perform(put("/profiles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProfile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldDeleteProfile() throws Exception {
        doNothing().when(service).deleteProfile(1L);

        mockMvc.perform(delete("/profiles/1"))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteProfile(1L);
    }

    @Test
    void shouldGetCalorieNeed() throws Exception {
        when(service.getProfile(1L)).thenReturn(testProfile);
        when(service.calculateCalorieNeed(testProfile)).thenReturn(2016.0);

        mockMvc.perform(get("/profiles/1/calorie-need"))
                .andExpect(status().isOk())
                .andExpect(content().string("2016.0"));
    }

    @Test
    void shouldGetWaterNeed() throws Exception {
        when(service.getProfile(1L)).thenReturn(testProfile);
        when(service.calculateWaterNeed(testProfile)).thenReturn(2.45);

        mockMvc.perform(get("/profiles/1/water-need"))
                .andExpect(status().isOk())
                .andExpect(content().string("2.45"));
    }
}