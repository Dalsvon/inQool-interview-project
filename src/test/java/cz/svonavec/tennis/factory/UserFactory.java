package cz.svonavec.tennis.factory;

import cz.svonavec.tennis.models.dtos.LoginUserDTO;
import cz.svonavec.tennis.models.dtos.UserDTO;
import cz.svonavec.tennis.models.dtos.UserRegisterDTO;
import cz.svonavec.tennis.models.dtos.UserUpdateDTO;
import cz.svonavec.tennis.models.entities.Role;
import cz.svonavec.tennis.models.entities.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserFactory {
    public static User createUser() {
        User user = new User();
        user.setPhoneNumber("+421123456789");
        user.setName("John Doe");
        user.setPassword("password123");
        user.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));

        List<Role> roles = new ArrayList<>();
        roles.add(Role.USER);
        user.setRoles(roles);

        return user;
    }

    public static UserDTO createUserDTO() {
        UserDTO user = new UserDTO();
        user.setPhoneNumber("+421123456789");
        user.setName("John Doe");
        user.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));

        List<Role> roles = new ArrayList<>();
        roles.add(Role.USER);
        user.setRoles(roles);

        return user;
    }

    public static UserDTO createUserDTORest() {
        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setPhoneNumber("+421123456789");
        user.setName("John Doe");
        user.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));

        List<Role> roles = new ArrayList<>();
        roles.add(Role.USER);
        user.setRoles(roles);

        return user;
    }

    public static UserUpdateDTO createUserUpdateDTORest() {
        UserUpdateDTO user = new UserUpdateDTO();
        user.setId(1L);
        user.setName("John Doe");
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        user.setRoles(roles);

        return user;
    }

    public static User createUser(String phoneNumber, String name, String password) {
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setName(name);
        user.setPassword(password);
        user.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));

        List<Role> roles = new ArrayList<>();
        roles.add(Role.USER);
        user.setRoles(roles);

        return user;
    }

    public static User createAdminUser() {
        User user = new User();
        user.setPhoneNumber("+421987654321");
        user.setName("Admin User");
        user.setPassword("adminpass123");
        user.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));

        List<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        user.setRoles(roles);

        return user;
    }

    public static LoginUserDTO createLoginDTO() {
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setPhoneNumber("+420907123456");
        loginUserDTO.setPassword("Password123");
        return loginUserDTO;
    }

    public static UserRegisterDTO createRegisterDTO() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setPhoneNumber("+420907123456");
        userRegisterDTO.setName("John Doe");
        userRegisterDTO.setPassword("Password123");
        return userRegisterDTO;
    }
}
