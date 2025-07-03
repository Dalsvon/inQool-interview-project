package cz.svonavec.tennis.repository;

import cz.svonavec.tennis.models.entities.User;

import java.util.List;

public interface UserRepository {
    User find(long id);
    User findByPhoneNumber(String phoneNumber);
    List<User> findAll();
    User update(User user);
    User register(User user);
    User delete(User user);
}
