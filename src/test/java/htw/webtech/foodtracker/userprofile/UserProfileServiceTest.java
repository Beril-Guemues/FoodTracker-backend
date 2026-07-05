package htw.webtech.foodtracker.userprofile;

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
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserProfileRepository repository;

    @InjectMocks
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

    // ===== getAllProfiles =====

    @Test
    void shouldGetAllProfiles() {
        when(repository.findAll()).thenReturn(List.of(testProfile));

        List<UserProfile> result = service.getAllProfiles();

        assertThat(result).hasSize(1);
        verify(repository, times(1)).findAll();
    }

    // ===== getProfile =====

    @Test
    void shouldGetProfile() {
        when(repository.findById(1L)).thenReturn(Optional.of(testProfile));

        UserProfile result = service.getProfile(1L);

        assertThat(result.getGender()).isEqualTo("male");
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowWhenProfileNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getProfile(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("UserProfile not found with id: 99");
    }

    // ===== saveProfile =====

    @Test
    void shouldSaveValidProfile() {
        when(repository.save(any(UserProfile.class))).thenReturn(testProfile);

        UserProfile result = service.saveProfile(testProfile);

        assertThat(result.getWeight()).isEqualTo(70);
        verify(repository, times(1)).save(testProfile);
    }

    @Test
    void shouldThrowWhenWeightIsZeroOrNegative() {
        testProfile.setWeight(0);

        assertThatThrownBy(() -> service.saveProfile(testProfile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Weight must be greater than 0");

        verify(repository, never()).save(any(UserProfile.class));
    }

    @Test
    void shouldThrowWhenGenderIsEmpty() {
        testProfile.setGender("  ");

        assertThatThrownBy(() -> service.saveProfile(testProfile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Gender cannot be empty");

        verify(repository, never()).save(any(UserProfile.class));
    }

    @Test
    void shouldThrowWhenGenderIsInvalid() {
        testProfile.setGender("other");

        assertThatThrownBy(() -> service.saveProfile(testProfile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Gender must be 'male' or 'female'");

        verify(repository, never()).save(any(UserProfile.class));
    }

    @Test
    void shouldThrowWhenAgeOutOfRange() {
        testProfile.setAge(0);

        assertThatThrownBy(() -> service.saveProfile(testProfile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Age must be between 1 and 120");

        verify(repository, never()).save(any(UserProfile.class));
    }

    @Test
    void shouldThrowWhenAgeAboveMax() {
        testProfile.setAge(121);

        assertThatThrownBy(() -> service.saveProfile(testProfile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Age must be between 1 and 120");

        verify(repository, never()).save(any(UserProfile.class));
    }

    @Test
    void shouldThrowWhenHeightIsZeroOrNegative() {
        testProfile.setHeight(0);

        assertThatThrownBy(() -> service.saveProfile(testProfile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Height must be greater than 0");

        verify(repository, never()).save(any(UserProfile.class));
    }

    // ===== updateProfile =====

    @Test
    void shouldUpdateExistingProfile() {
        UserProfile updatedDetails = new UserProfile();
        updatedDetails.setWeight(80);
        updatedDetails.setGender("female");
        updatedDetails.setAge(25);
        updatedDetails.setHeight(170);

        when(repository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(repository.save(any(UserProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserProfile result = service.updateProfile(1L, updatedDetails);

        assertThat(result.getWeight()).isEqualTo(80);
        assertThat(result.getGender()).isEqualTo("female");
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(UserProfile.class));
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentProfile() {
        UserProfile updatedDetails = new UserProfile();
        updatedDetails.setWeight(80);
        updatedDetails.setGender("female");
        updatedDetails.setAge(25);
        updatedDetails.setHeight(170);

        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateProfile(99L, updatedDetails))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("UserProfile not found with id: 99");

        verify(repository, never()).save(any(UserProfile.class));
    }

    // ===== deleteProfile =====

    @Test
    void shouldDeleteExistingProfile() {
        when(repository.existsById(1L)).thenReturn(true);

        service.deleteProfile(1L);

        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentProfile() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteProfile(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("UserProfile not found with id: 99");

        verify(repository, never()).deleteById(anyLong());
    }

    // ===== calculateCalorieNeed =====

    @Test
    void shouldCalculateCalorieNeedForMale() {
        // BMR = 10*70 + 6.25*180 - 5*30 + 5 = 700 + 1125 - 150 + 5 = 1680
        // Kalorienbedarf = 1680 * 1.2 = 2016
        double result = service.calculateCalorieNeed(testProfile);

        assertThat(result).isCloseTo(2016.0, within(0.01));
    }

    @Test
    void shouldCalculateCalorieNeedForFemale() {
        testProfile.setGender("female");
        // BMR = 10*70 + 6.25*180 - 5*30 - 161 = 700 + 1125 - 150 - 161 = 1514
        // Kalorienbedarf = 1514 * 1.2 = 1816.8
        double result = service.calculateCalorieNeed(testProfile);

        assertThat(result).isCloseTo(1816.8, within(0.01));
    }

    // ===== calculateWaterNeed =====

    @Test
    void shouldCalculateWaterNeed() {
        // Wasserbedarf = 70 * 0.035 = 2.45
        double result = service.calculateWaterNeed(testProfile);

        assertThat(result).isCloseTo(2.45, within(0.001));
    }
}