package cz.svonavec.tennis.service;

import cz.svonavec.tennis.exception.BadRequestException;
import cz.svonavec.tennis.exception.ResourceNotFoundException;
import cz.svonavec.tennis.factory.CourtFactory;
import cz.svonavec.tennis.factory.SurfaceTypeFactory;
import cz.svonavec.tennis.factory.UserFactory;
import cz.svonavec.tennis.models.entities.Court;
import cz.svonavec.tennis.models.entities.Role;
import cz.svonavec.tennis.models.entities.SurfaceType;
import cz.svonavec.tennis.models.entities.User;
import cz.svonavec.tennis.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void find_userFound_returnsUser() {
        // Arrange
        when(userRepository.find(1L)).thenReturn(UserFactory.createUser());

        // Act
        User user = userService.findById(1L);

        // Assert
        assertThat(user).isEqualTo(UserFactory.createUser());
    }

    @Test
    void find_userNotFound_throwsResourceNotFoundException() {
        assertThrows(ResourceNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void findByPhoneNumber_userFound_returnsUser() {
        // Arrange
        when(userRepository.findByPhoneNumber("+421123456789")).thenReturn(UserFactory.createUser());

        // Act
        User user = userService.findByPhoneNumber("+421123456789");

        // Assert
        assertThat(user).isEqualTo(UserFactory.createUser());
    }

    @Test
    void findByPhoneNumber_userNotFound_throwsResourceNotFoundException() {
        assertThrows(ResourceNotFoundException.class, () -> userService.findByPhoneNumber("+421123456789"));
    }

    @Test
    void loadUserByUsername_userFound_returnsUser() {
        // Arrange
        when(userRepository.findByPhoneNumber("+421123456789")).thenReturn(UserFactory.createUser());

        // Act
        UserDetails user = userService.loadUserByUsername("+421123456789");

        // Assert
        assertThat(user).isEqualTo(UserFactory.createUser());
    }

    @Test
    void loadUserByUsername_userNotFound_throwsResourceNotFoundException() {
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("+421123456789"));
    }

    @Test
    void findAll_twoUsersFound_returnUsers() {
        List<User> users = List.of(UserFactory.createAdminUser(), UserFactory.createUser());
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> userList = userService.findAll();

        // Assert
        assertThat(userList.size()).isEqualTo(2);
        assertThat(userList.getFirst().getName()).isEqualTo("Admin User");
    }

    @Test
    void register_userCreated_returnsUser() {
        // Arrange
        User user = UserFactory.createUser();
        user.setId(0L);
        user.setPassword("hashed");
        user.setRoles(List.of(Role.USER));

        User userCreate = UserFactory.createUser();
        user.setId(0L);
        when(userRepository.register(user)).thenReturn(user);
        when(passwordEncoder.encode(any(String.class))).thenReturn("hashed");

        // Act
        User userCreated = userService.register(userCreate, "Password123");

        // Assert
        assertThat(userCreated.getPassword()).isEqualTo("hashed");
        assertThat(userCreated.getRoles()).isEqualTo(List.of(Role.USER));
        verify(userRepository).register(user);
    }

    @Test
    void register_userHasId_throwsBadRequestException() {
        User user = UserFactory.createUser();
        user.setId(1L);
        assertThrows(BadRequestException.class, () -> userService.register(user, "Password123"));
    }

    @Test
    void register_userTooShortPassword_throwsBadRequestException() {
        User user = UserFactory.createUser();
        assertThrows(BadRequestException.class, () -> userService.register(user, "P123"));
    }

    @Test
    void register_userPasswordHasNoCapitals_throwsBadRequestException() {
        User user = UserFactory.createUser();
        assertThrows(BadRequestException.class, () -> userService.register(user, "dasdsadasasd123"));
    }

    @Test
    void register_userPasswordHasNoNumbers_throwsBadRequestException() {
        User user = UserFactory.createUser();
        assertThrows(BadRequestException.class, () -> userService.register(user, "dasdsadasasdA"));
    }

    @Test
    void register_userBadPhone_throwsBadRequestException() {
        User user = UserFactory.createUser();
        user.setPhoneNumber("dasdas");
        assertThrows(BadRequestException.class, () -> userService.register(user, "Password123"));
    }

    @Test
    void update_userUpdated_returnsUser() {
        // Arrange
        User userUpdate = UserFactory.createUser();
        userUpdate.setRoles(List.of(Role.USER, Role.ADMIN));
        userUpdate.setName("Pokemon");
        userUpdate.setId(1L);

        when(userRepository.find(1L)).thenReturn(UserFactory.createUser());
        when(userRepository.update(userUpdate)).thenReturn(userUpdate);

        // Act
        User user = userService.update(userUpdate);

        // Assert
        assertThat(user).isEqualTo(userUpdate);
        assertThat(user.getName()).isEqualTo("Pokemon");
        assertThat(user.getRoles()).isEqualTo(List.of(Role.USER, Role.ADMIN));
        verify(userRepository).update(userUpdate);
    }

    @Test
    void update_noUpdateFields_throwsBadRequestException() {
        // Arrange
        User user = UserFactory.createUser();
        user.setName(null);
        user.setRoles(null);
        user.setId(1L);
        when(userRepository.find(1L)).thenReturn(UserFactory.createUser());

        // Act
        assertThrows(BadRequestException.class, () -> userService.update(user));
    }

    @Test
    void delete_userDeleted_returnsUser() {
        // Arrange
        User userDeleted = UserFactory.createUser();
        userDeleted.setId(1L);
        userDeleted.setDeletedAt(LocalDateTime.now());


        when(userRepository.find(1L)).thenReturn(UserFactory.createUser());
        when(userRepository.delete(UserFactory.createUser())).thenReturn(userDeleted);

        // Act
        User user = userService.delete(1L);

        // Assert
        assertThat(user.getDeletedAt()).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        verify(userRepository).find(1L);
    }
}
