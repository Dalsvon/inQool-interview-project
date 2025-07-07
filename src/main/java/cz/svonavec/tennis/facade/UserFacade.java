package cz.svonavec.tennis.facade;

import cz.svonavec.tennis.mapper.UserMapper;
import cz.svonavec.tennis.models.dtos.UserDTO;
import cz.svonavec.tennis.models.dtos.UserRegisterDTO;
import cz.svonavec.tennis.models.dtos.UserUpdateDTO;
import cz.svonavec.tennis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserFacade {
    private final UserMapper userMapper;

    private final UserService userService;

    @Autowired
    public UserFacade(UserMapper userMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public UserDTO findById(long id) {
        return userMapper.mapToDTO(userService.findById(id));
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        return userMapper.mapToDTOList(userService.findAll());
    }

    @Transactional(readOnly = true)
    public UserDTO findByPhoneNumber(String phoneNumber) {
        return userMapper.mapToDTO(userService.findByPhoneNumber(phoneNumber));
    }

    @Transactional
    public UserDTO register(UserRegisterDTO dto) {
        return userMapper.mapToDTO(userService.register(userMapper.mapToEntity(dto), dto.getPassword()));
    }

    @Transactional
    public UserDTO update(UserUpdateDTO dto) {
        return userMapper.mapToDTO(userService.update(userMapper.mapToEntity(dto)));
    }

    @Transactional
    public UserDTO delete(long id) {
        return userMapper.mapToDTO(userService.delete(id));
    }
}
