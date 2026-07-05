package htw.webtech.foodtracker.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("test@example.com", "geheim123");
        testUser.setId(1L);
    }

    // ===== register =====

    @Test
    void shouldRegisterNewUser() {
        when(repository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(repository.save(any(User.class))).thenReturn(testUser);

        User result = service.register("test@example.com", "geheim123");

        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getId()).isEqualTo(1L);
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowWhenEmailAlreadyRegistered() {
        when(repository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> service.register("test@example.com", "neuesPasswort"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email already registered");

        verify(repository, never()).save(any(User.class));
    }

    // ===== login =====

    @Test
    void shouldLoginWithCorrectCredentials() {
        when(repository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        User result = service.login("test@example.com", "geheim123");

        assertThat(result.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void shouldThrowWhenUserNotFoundOnLogin() {
        when(repository.findByEmail("unbekannt@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.login("unbekannt@example.com", "irgendwas"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void shouldThrowWhenPasswordIsWrong() {
        when(repository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> service.login("test@example.com", "falschesPasswort"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid password");
    }
}