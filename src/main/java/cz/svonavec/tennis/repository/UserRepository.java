package cz.svonavec.tennis.repository;

import cz.svonavec.tennis.models.entities.User;

import java.util.List;

public interface UserRepository {
    /**
     * Finds and returns user from the database with corresponding id
     *
     * @param id id of the user
     * @return found user
     */
    User find(long id);

    /**
     * Finds and returns user from the database with corresponding unique phone number
     *
     * @param phoneNumber unique phone number for the user
     * @return found user
     */
    User findByPhoneNumber(String phoneNumber);

    /**
     * Finds and returns all surfaces in the database (undeleted)
     *
     * @return all users
     */
    List<User> findAll();

    /**
     * Updates the user with given data (changes information in user with the same id) in the database
     *
     * @param user user data
     * @return updated user
     */
    User update(User user);

    /**
     * Creates and saves user from given data
     *
     * @param user user data
     * @return registered user
     */
    User register(User user);

    /**
     * Deletes user from the database. Performed as SOFT delete by assigning deletedAt field
     *
     * @param user user data
     * @return deleted user
     */
    User delete(User user);
}
