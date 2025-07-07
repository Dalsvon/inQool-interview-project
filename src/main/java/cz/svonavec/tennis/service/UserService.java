package cz.svonavec.tennis.service;

import cz.svonavec.tennis.exception.BadRequestException;
import cz.svonavec.tennis.exception.ResourceNotFoundException;
import cz.svonavec.tennis.models.entities.Role;
import cz.svonavec.tennis.models.entities.User;
import cz.svonavec.tennis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    public final UserRepository userRepository;

    public final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
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
    public UserDetails loadUserByUsername(String username) {
        try {
            return findByPhoneNumber(username);
        } catch (ResourceNotFoundException e) {
            throw new UsernameNotFoundException("User not found with phone number: " + username, e);
        }
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Registers a new user to the system. All users are created with Role User. User password must be at least 8
     * characters long and contain digits and capital letters. Phone number must be unique and never before used in
     * the database. After all conditions are checked, the password is encoded and the user is registered.
     *
     * @param user user data
     * @param unhashedPassword unhashed password
     * @return registered user
     */
    @Transactional
    public User register(User user, String unhashedPassword) {
        if (user.getId() != 0 || user.getPhoneNumber() == null) {
            throw new BadRequestException("Trying to create a court with set id or no phone.");
        }
        if (unhashedPassword.length() < 8 || unhashedPassword.chars().noneMatch(Character::isDigit) ||
                unhashedPassword.chars().noneMatch(Character::isUpperCase)) {
            throw new BadRequestException("Password must contain at least 8 characters, numbers and capitals.");
        }
        if (!user.validatePhoneNumber()) {
            throw new BadRequestException("Phone number does not followed allowed formats");
        }
        if (userRepository.findByPhoneNumber(user.getPhoneNumber()) != null) {
            throw new BadRequestException("This phone number was already used in another account.");
        }
        String hashedPassword = passwordEncoder.encode(unhashedPassword);
        user.setPassword(hashedPassword);
        user.setRoles(List.of(Role.USER));
        return userRepository.register(user);
    }

    @Transactional
    public User update(User user) {
        User foundUser = findById(user.getId());
        if ((user.getRoles() != null && user.getRoles().size() != 0) || user.getName() != null) {
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
        return userRepository.delete(user);
    }
}
