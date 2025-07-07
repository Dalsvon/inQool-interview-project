package cz.svonavec.tennis.mapper;

import cz.svonavec.tennis.models.dtos.UserDTO;
import cz.svonavec.tennis.models.dtos.UserRegisterDTO;
import cz.svonavec.tennis.models.dtos.UserUpdateDTO;
import cz.svonavec.tennis.models.entities.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserMapper {
    public User mapToEntity(UserRegisterDTO dto) {
        User user = new User();

        user.setId(0);
        user.setName(dto.getName());
        user.setPhoneNumber(dto.getPhoneNumber());

        return user;
    }

    public User mapToEntity(UserUpdateDTO dto) {
        User user = new User();

        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setRoles(dto.getRoles());

        return user;
    }

    public UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();

        dto.setId(user.getId());
        dto.setDeletedAt(user.getDeletedAt());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setName(user.getName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRoles(user.getRoles());

        return dto;
    }

    public List<UserDTO> mapToDTOList(List<User> users) {
        List<UserDTO> DTOs = new ArrayList<>();
        for (User user : users) {
            DTOs.add(mapToDTO(user));
        }
        return DTOs;
    }
}
