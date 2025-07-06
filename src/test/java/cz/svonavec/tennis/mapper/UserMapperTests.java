package cz.svonavec.tennis.mapper;

import cz.svonavec.tennis.factory.UserFactory;
import cz.svonavec.tennis.models.dtos.UserDTO;
import cz.svonavec.tennis.models.dtos.UserRegisterDTO;
import cz.svonavec.tennis.models.dtos.UserUpdateDTO;
import cz.svonavec.tennis.models.entities.Role;
import cz.svonavec.tennis.models.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class UserMapperTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void mapToEntity_correctRegisterDTO_successfullyMapsToUserEntity() {
        // Arrange
        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setName("John Doe");
        registerDTO.setPhoneNumber("+421123456789");

        // Act
        User mappedUser = userMapper.mapToEntity(registerDTO);

        // Assert
        assertThat(mappedUser).isNotNull();
        assertThat(mappedUser.getId()).isEqualTo(0);
        assertThat(mappedUser.getName()).isEqualTo("John Doe");
        assertThat(mappedUser.getPhoneNumber()).isEqualTo("+421123456789");
        assertThat(mappedUser.getPassword()).isNull();
        assertThat(mappedUser.getRoles()).isEqualTo(List.of());
        assertThat(mappedUser.getCreatedAt()).isNull();
        assertThat(mappedUser.getDeletedAt()).isNull();
    }

    @Test
    void mapToEntity_correctUpdateDTO_successfullyMapsToUserEntity() {
        // Arrange
        List<Role> roles = new ArrayList<>();
        roles.add(Role.USER);
        roles.add(Role.ADMIN);

        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setId(1L);
        updateDTO.setName("Jane Smith");
        updateDTO.setRoles(roles);

        // Act
        User mappedUser = userMapper.mapToEntity(updateDTO);

        // Assert
        assertThat(mappedUser).isNotNull();
        assertThat(mappedUser.getId()).isEqualTo(1L);
        assertThat(mappedUser.getName()).isEqualTo("Jane Smith");
        assertThat(mappedUser.getRoles().size()).isEqualTo(2);
        assertThat(mappedUser.getPhoneNumber()).isNull();
        assertThat(mappedUser.getPassword()).isNull();
        assertThat(mappedUser.getCreatedAt()).isNull();
        assertThat(mappedUser.getDeletedAt()).isNull();
    }

    @Test
    void mapToDTO_correctEntity_successfullyMapsToUserDTO() {
        // Arrange
        User user = UserFactory.createUser("+421987654321", "Alice Brown", "password123");
        user.setId(5L);

        LocalDateTime createdAt = LocalDateTime.now().plusDays(2);
        user.setCreatedAt(createdAt);
        user.setDeletedAt(LocalDateTime.now().plusDays(10));

        // Act
        UserDTO mappedDTO = userMapper.mapToDTO(user);

        // Assert
        assertThat(mappedDTO).isNotNull();
        assertThat(mappedDTO.getId()).isEqualTo(5L);
        assertThat(mappedDTO.getName()).isEqualTo("Alice Brown");
        assertThat(mappedDTO.getPhoneNumber()).isEqualTo("+421987654321");
        assertThat(mappedDTO.getRoles().size()).isEqualTo(1);
        assertThat(mappedDTO.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void mapToDTOList_correctEntityList_successfullyMapsToUserDTOList() {
        // Arrange
        User user1 = UserFactory.createUser("+421111111111", "John", "pass1");
        user1.setId(1L);

        User user2 = UserFactory.createUser("+421222222222", "Jane", "pass2");
        user2.setId(2L);

        List<User> users = List.of(user1, user2);

        // Act
        List<UserDTO> mappedDTOs = userMapper.mapToDTOList(users);

        // Assert
        assertThat(mappedDTOs).isNotNull();
        assertThat(mappedDTOs.size()).isEqualTo(2);

        UserDTO dto1 = mappedDTOs.get(0);
        assertThat(dto1.getId()).isEqualTo(1L);
        assertThat(dto1.getName()).isEqualTo("John");
        assertThat(dto1.getPhoneNumber()).isEqualTo("+421111111111");

        UserDTO dto2 = mappedDTOs.get(1);
        assertThat(dto2.getId()).isEqualTo(2L);
        assertThat(dto2.getName()).isEqualTo("Jane");
        assertThat(dto2.getPhoneNumber()).isEqualTo("+421222222222");
    }
}
