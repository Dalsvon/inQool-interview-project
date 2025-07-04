package cz.svonavec.tennis.service;

import cz.svonavec.tennis.exception.BadRequestException;
import cz.svonavec.tennis.exception.ResourceNotFoundException;
import cz.svonavec.tennis.models.entities.Reservation;
import cz.svonavec.tennis.models.entities.Role;
import cz.svonavec.tennis.models.entities.User;
import cz.svonavec.tennis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    public final UserRepository userRepository;

    public final ReservationService reservationService;

    public final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, ReservationService reservationService, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.reservationService = reservationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public User findById(long id) {
        User user = userRepository.find(id);
        if (user == null || user.getDeletedAt() != null) {
            throw new ResourceNotFoundException("Couldn't find user with this id.");
        }
        return user;
    }

    @Transactional(readOnly = true)
    public User findByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null || user.getDeletedAt() != null) {
            throw new ResourceNotFoundException("Couldn't find user with this id.");
        }
        return user;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public boolean login(String phoneNumber, String password) {
        User user = findByPhoneNumber(phoneNumber);
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Transactional
    public User register(User user, String unhashedPassword) {
        if (unhashedPassword.length() < 8 || unhashedPassword.chars().noneMatch(Character::isDigit) ||
                unhashedPassword.chars().noneMatch(Character::isUpperCase)) {
            throw new BadRequestException("Password must contain at least 8 characters, numbers and capitals.");
        }
        String hashedPassword = passwordEncoder.encode(unhashedPassword);
        user.setPassword(hashedPassword);
        user.setRoles(List.of(Role.USER));
        return userRepository.register(user);
    }

    @Transactional
    public User update(User user) {
        User foundUser = findById(user.getId());
        if (user.getRoles() != null || user.getName() != null) {
            if (user.getName() != null) {
                foundUser.setName(user.getName());
            }
            if (user.getRoles() != null) {
                foundUser.setRoles(user.getRoles());
            }
        } else {
            throw new BadRequestException("At least one query field must be used.");
        }
        return userRepository.update(foundUser);
    }

    @Transactional
    public User delete(long id) {
        User user = findById(id);
        List<Reservation> reservations = reservationService.findByPhone(user.getPhoneNumber(), false);
        for (Reservation reservation : reservations) {
            reservationService.delete(reservation.getId());
        }
        return userRepository.delete(user);
    }
}
