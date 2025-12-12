package com.example.userservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("1");
        user.setName("Alice");
        user.setEmail("alice@example.com");
    }

    @Test
    void createUser_ShouldReturnSavedUser() {
        // Given
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User created = userService.createUser(user);

        // Then
        assertThat(created).isNotNull();
        assertThat(created.getName()).isEqualTo("Alice");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getAllUsers_ShouldReturnUserList() {
        // Given
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        // When
        List<User> users = userService.getAllUsers();

        // Then
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getName()).isEqualTo("Alice");
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        // When
        User found = userService.getUserById("1");

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo("1");
    }

    @Test
    void getUserById_WhenUserMissing_ShouldThrowException() {
        // Given
        when(userRepository.findById("99")).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> userService.getUserById("99"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }
}
