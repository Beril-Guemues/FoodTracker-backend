package htw.webtech.foodtracker.goal;

import htw.webtech.foodtracker.userprofile.UserProfile;
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

@WebMvcTest(GoalController.class)
class GoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper objectMapper;

    @MockitoBean
    private GoalService service;

    private UserProfile testUserProfile;
    private Goal testGoal;

    @BeforeEach
    void setUp() {
        testUserProfile = new UserProfile();
        testUserProfile.setId(1L);
        testUserProfile.setWeight(70);
        testUserProfile.setGender("male");
        testUserProfile.setAge(30);
        testUserProfile.setHeight(180);

        testGoal = new Goal();
        testGoal.setId(1L);
        testGoal.setType("abnehmen");
        testGoal.setUserProfile(testUserProfile);
    }

    @Test
    void shouldGetAllGoals() throws Exception {
        when(service.getAllGoals()).thenReturn(List.of(testGoal));

        mockMvc.perform(get("/goals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].type").value("abnehmen"))
                .andExpect(jsonPath("$[0].userProfile.id").value(1L));
    }

    @Test
    void shouldGetGoalsByUserProfileId() throws Exception {
        when(service.getGoalsByUserProfileId(1L)).thenReturn(List.of(testGoal));

        mockMvc.perform(get("/goals/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("abnehmen"))
                .andExpect(jsonPath("$[0].userProfile.id").value(1L));
    }

    @Test
    void shouldGetGoalById() throws Exception {
        when(service.getGoalById(1L)).thenReturn(testGoal);

        mockMvc.perform(get("/goals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.type").value("abnehmen"));
    }

    @Test
    void shouldCreateGoal() throws Exception {
        when(service.saveGoal(any(Goal.class))).thenReturn(testGoal);

        mockMvc.perform(post("/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testGoal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.type").value("abnehmen"));
    }

    @Test
    void shouldUpdateGoal() throws Exception {
        when(service.updateGoal(eq(1L), any(Goal.class))).thenReturn(testGoal);

        mockMvc.perform(put("/goals/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testGoal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldDeleteGoal() throws Exception {
        doNothing().when(service).deleteGoal(1L);

        mockMvc.perform(delete("/goals/1"))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteGoal(1L);
    }
}