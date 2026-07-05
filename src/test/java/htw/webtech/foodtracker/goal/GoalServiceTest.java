package htw.webtech.foodtracker.goal;

import htw.webtech.foodtracker.userprofile.UserProfile;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceTest {

    @Mock
    private GoalRepository repository;

    @InjectMocks
    private GoalService service;

    private UserProfile testUserProfile;
    private Goal testGoal;

    @BeforeEach
    void setUp() {
        testUserProfile = new UserProfile();
        testUserProfile.setId(1L);

        testGoal = new Goal();
        testGoal.setId(1L);
        testGoal.setType("abnehmen");
        testGoal.setUserProfile(testUserProfile);
    }

    // ===== getAllGoals =====

    @Test
    void shouldGetAllGoals() {
        when(repository.findAll()).thenReturn(List.of(testGoal));

        List<Goal> result = service.getAllGoals();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getType()).isEqualTo("abnehmen");
        verify(repository, times(1)).findAll();
    }

    // ===== getGoalById =====

    @Test
    void shouldGetGoalById() {
        when(repository.findById(1L)).thenReturn(Optional.of(testGoal));

        Goal result = service.getGoalById(1L);

        assertThat(result.getType()).isEqualTo("abnehmen");
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowWhenGoalByIdNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getGoalById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Goal not found with id: 99");
    }

    // ===== getGoalsByUserProfileId =====

    @Test
    void shouldGetGoalsByUserProfileId() {
        when(repository.findByUserProfileId(1L)).thenReturn(List.of(testGoal));

        List<Goal> result = service.getGoalsByUserProfileId(1L);

        assertThat(result).hasSize(1);
        verify(repository, times(1)).findByUserProfileId(1L);
    }

    // ===== saveGoal =====

    @Test
    void shouldSaveValidGoal() {
        when(repository.save(any(Goal.class))).thenReturn(testGoal);

        Goal result = service.saveGoal(testGoal);

        assertThat(result.getType()).isEqualTo("abnehmen");
        verify(repository, times(1)).save(testGoal);
    }

    @Test
    void shouldThrowWhenSavingGoalWithEmptyType() {
        testGoal.setType("  ");

        assertThatThrownBy(() -> service.saveGoal(testGoal))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Goal type cannot be empty");

        verify(repository, never()).save(any(Goal.class));
    }

    @Test
    void shouldThrowWhenSavingGoalWithInvalidType() {
        testGoal.setType("faulenzen");

        assertThatThrownBy(() -> service.saveGoal(testGoal))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid goal type");

        verify(repository, never()).save(any(Goal.class));
    }

    @Test
    void shouldAcceptAllValidGoalTypes() {
        when(repository.save(any(Goal.class))).thenAnswer(invocation -> invocation.getArgument(0));

        for (String type : List.of("abnehmen", "zunehmen", "muskeln_aufbauen", "gesund_ernaehren")) {
            testGoal.setType(type);
            Goal result = service.saveGoal(testGoal);
            assertThat(result.getType()).isEqualTo(type);
        }

        verify(repository, times(4)).save(any(Goal.class));
    }

    @Test
    void shouldThrowWhenSavingGoalWithoutUserProfile() {
        testGoal.setUserProfile(null);

        assertThatThrownBy(() -> service.saveGoal(testGoal))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("UserProfile cannot be null");

        verify(repository, never()).save(any(Goal.class));
    }

    // ===== updateGoal =====

    @Test
    void shouldUpdateExistingGoal() {
        UserProfile newUserProfile = new UserProfile();
        newUserProfile.setId(2L);

        Goal updatedDetails = new Goal();
        updatedDetails.setType("zunehmen");
        updatedDetails.setUserProfile(newUserProfile);

        when(repository.findById(1L)).thenReturn(Optional.of(testGoal));
        when(repository.save(any(Goal.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Goal result = service.updateGoal(1L, updatedDetails);

        assertThat(result.getType()).isEqualTo("zunehmen");
        assertThat(result.getUserProfile().getId()).isEqualTo(2L);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Goal.class));
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentGoal() {
        Goal updatedDetails = new Goal();
        updatedDetails.setType("zunehmen");
        updatedDetails.setUserProfile(testUserProfile);

        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateGoal(99L, updatedDetails))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Goal not found with id: 99");

        verify(repository, never()).save(any(Goal.class));
    }

    @Test
    void shouldThrowWhenUpdatingGoalWithInvalidType() {
        Goal updatedDetails = new Goal();
        updatedDetails.setType("schlafen");
        updatedDetails.setUserProfile(testUserProfile);

        when(repository.findById(1L)).thenReturn(Optional.of(testGoal));

        assertThatThrownBy(() -> service.updateGoal(1L, updatedDetails))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid goal type");

        verify(repository, never()).save(any(Goal.class));
    }

    // ===== deleteGoal =====

    @Test
    void shouldDeleteExistingGoal() {
        when(repository.existsById(1L)).thenReturn(true);

        service.deleteGoal(1L);

        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentGoal() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteGoal(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Goal not found with id: 99");

        verify(repository, never()).deleteById(anyLong());
    }
}