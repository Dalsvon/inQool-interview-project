package cz.svonavec.tennis.facade;

import cz.svonavec.tennis.factory.UserFactory;
import cz.svonavec.tennis.mapper.UserMapper;
import cz.svonavec.tennis.models.dtos.UserDTO;
import cz.svonavec.tennis.models.dtos.UserRegisterDTO;
import cz.svonavec.tennis.models.dtos.UserUpdateDTO;
import cz.svonavec.tennis.models.entities.Role;
import cz.svonavec.tennis.models.entities.User;
import cz.svonavec.tennis.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserFacadeTests {
    @Mock
    private UserMapper userMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserFacade userFacade;

    @Test
    void findById_findsUser_returnsUser() {
        // Arrange
        when(userService.findById(1L)).thenReturn(UserFactory.createUser());
        when(userMapper.mapToDTO(UserFactory.createUser())).thenReturn(UserFactory.createUserDTO());

        // Act
        UserDTO userDTO = userFacade.findById(1L);

        // Assert
        verify(userService).findById(1);
        assertThat(userDTO.getPhoneNumber()).isEqualTo(UserFactory.createUser().getPhoneNumber());
    }

    @Test
    void findByPhoneNumber_findsUser_returnsUser() {
        // Arrange
        when(userService.findByPhoneNumber("+421123456789")).thenReturn(UserFactory.createUser());
        when(userMapper.mapToDTO(UserFactory.createUser())).thenReturn(UserFactory.createUserDTO());

        // Act
        UserDTO userDTO = userFacade.findByPhoneNumber("+421123456789");

        // Assert
        verify(userService).findByPhoneNumber("+421123456789");
        assertThat(userDTO.getPhoneNumber()).isEqualTo(UserFactory.createUser().getPhoneNumber());
    }

    @Test
    void findAll_findsAllUsers_returnsUsers() {
        // Arrange
        List<User> users = List.of(UserFactory.createUser(), UserFactory.createUser());
        List<UserDTO> userDTOS = List.of(UserFactory.createUserDTO(), UserFactory.createUserDTO());
        when(userService.findAll()).thenReturn(users);
        when(userMapper.mapToDTOList(users)).thenReturn(userDTOS);

        // Act
        List<UserDTO> result = userFacade.findAll();

        // Assert
        verify(userService).findAll();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void create_createsUser_returnsUser() {
        // Arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setName("John Doe");
        userRegisterDTO.setPhoneNumber("+421123456789");
        userRegisterDTO.setPassword("unhashedPassword");
        User user = new User();
        user.setName("John Doe");
        user.setPhoneNumber("+421123456789");
        when(userService.register(user, "unhashedPassword")).thenReturn(UserFactory.createUser());
        when(userMapper.mapToDTO(UserFactory.createUser())).thenReturn(UserFactory.createUserDTO());
        when(userMapper.mapToEntity(userRegisterDTO)).thenReturn(user);

        // Act
        UserDTO result = userFacade.register(userRegisterDTO);

        // Assert
        verify(userService).register(user, "unhashedPassword");
        assertThat(result.getPhoneNumber()).isEqualTo("+421123456789");
    }

    @Test
    void update_updatesUser_returnsUser() {
        // Arrange
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(1L, "John Doe", List.of(Role.USER));
        User user = UserFactory.createUser();
        when(userService.update(user)).thenReturn(user);
        when(userMapper.mapToEntity(userUpdateDTO)).thenReturn(user);
        when(userMapper.mapToDTO(user)).thenReturn(UserFactory.createUserDTO());

        // Act
        UserDTO userDTO = userFacade.update(userUpdateDTO);

        // Assert
        verify(userService).update(user);
        assertThat(userDTO.getPhoneNumber()).isEqualTo("+421123456789");
    }

    @Test
    void delete_deleteUser_returnsUser() {
        // Arrange
        LocalDateTime time = LocalDateTime.now();
        User user = UserFactory.createUser();
        user.setDeletedAt(time);
        when(userService.delete(1L)).thenReturn(user);
        UserDTO userDTO = UserFactory.createUserDTO();
        userDTO.setDeletedAt(time);
        when(userMapper.mapToDTO(user)).thenReturn(userDTO);

        // Act
        UserDTO userDTOFound = userFacade.delete(1L);

        // Assert
        verify(userService).delete(1);
        assertThat(userDTOFound.getDeletedAt()).isNotNull();
        assertThat(userDTOFound).isEqualTo(userDTO);
    }
}
