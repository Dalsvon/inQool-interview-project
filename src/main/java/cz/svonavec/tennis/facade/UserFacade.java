package cz.svonavec.tennis.facade;

import cz.svonavec.tennis.mapper.UserMapper;
import cz.svonavec.tennis.models.dtos.UserDTO;
import cz.svonavec.tennis.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserFacade {
    private final UserMapper userMapper;

    private final UserService userService;

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
}
