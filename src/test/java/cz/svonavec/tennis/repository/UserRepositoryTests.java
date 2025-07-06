package cz.svonavec.tennis.repository;

import cz.svonavec.tennis.factory.CourtFactory;
import cz.svonavec.tennis.factory.ReservationFactory;
import cz.svonavec.tennis.factory.SurfaceTypeFactory;
import cz.svonavec.tennis.factory.UserFactory;
import cz.svonavec.tennis.models.entities.Court;
import cz.svonavec.tennis.models.entities.Reservation;
import cz.svonavec.tennis.models.entities.SurfaceType;
import cz.svonavec.tennis.models.entities.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTests {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    @Transactional
    void setUp() {
        entityManager.createQuery("DELETE FROM Reservation").executeUpdate();
        entityManager.createQuery("DELETE FROM Court").executeUpdate();
        entityManager.createQuery("DELETE FROM SurfaceType").executeUpdate();
        entityManager.createQuery("DELETE FROM User").executeUpdate();
        entityManager.flush();
    }

    @Test
    @Transactional
    void find_userFound_successfullyReturnedUser() {
        // Arrange
        User user = UserFactory.createUser();
        entityManager.persist(user);
        entityManager.detach(user);

        // Act
        User foundUser = userRepository.find(user.getId());

        // Assert
        assertThat(foundUser.getId()).isEqualTo(user.getId());
        assertThat(user.getName()).isEqualTo("John Doe");
    }

    @Test
    @Transactional
    void find_userNotFound_successfullyReturnedNull() {
        // Act
        User foundUser = userRepository.find(1L);

        // Assert
        assertThat(foundUser).isNull();
    }

    @Test
    @Transactional
    void findByPhone_userFound_successfullyReturnedUser() {
        // Arrange
        User user = UserFactory.createUser();
        entityManager.persist(user);
        entityManager.detach(user);

        // Act
        User foundUser = userRepository.findByPhoneNumber(user.getPhoneNumber());

        // Assert
        assertThat(foundUser.getId()).isEqualTo(user.getId());
        assertThat(user.getName()).isEqualTo("John Doe");
    }

    @Test
    @Transactional
    void findByPhone_userNotFound_successfullyReturnedNull() {
        // Act
        User foundUser = userRepository.findByPhoneNumber("+421907123456");

        // Assert
        assertThat(foundUser).isNull();
    }

    @Test
    @Transactional
    void findAll_twoUsersFound_successfullyReturnedUsers() {
        // Arrange
        User user1 = UserFactory.createUser();
        User user2 = UserFactory.createAdminUser();
        entityManager.persist(user2);
        entityManager.persist(user1);

        // Act
        List<User> foundUsers = userRepository.findAll();

        // Assert
        assertThat(foundUsers).hasSize(2);
        assertThat(foundUsers.get(0).getName()).isEqualTo("Admin User");
    }

    @Test
    @Transactional
    void register_userCreatedSuccessfully_returnCreatedUser() {
        // Arrange
        User user = UserFactory.createUser();

        // Act
        User userRegistered = userRepository.register(user);

        // Assert
        assertThat(userRegistered).isEqualTo(user);
        assertThat(userRepository.find(userRegistered.getId())).isEqualTo(user);
    }

    @Test
    @Transactional
    void update_surfaceUpdatedSuccessfully_returnUpdatedSurface() {
        // Arrange
        User user = UserFactory.createUser();
        entityManager.persist(user);
        entityManager.detach(user);

        User userUpdate = new User();
        userUpdate.setId(user.getId());
        userUpdate.setName("NewName");

        // Act
        User updatedUser = userRepository.update(userUpdate);

        // Assert
        assertThat(updatedUser).isEqualTo(userUpdate);
        assertThat(userRepository.find(updatedUser.getId()).getName()).isEqualTo("NewName");
    }

    @Test
    @Transactional
    void delete_courtDeleteSuccessfully_deletedReservations() {
        // Arrange
        SurfaceType surfaceType = SurfaceTypeFactory.createSurfaceType();
        entityManager.persist(surfaceType);

        Court court = CourtFactory.createCourt(surfaceType);
        entityManager.persist(court);
        entityManager.detach(court);

        User user = UserFactory.createUser();
        entityManager.persist(user);
        entityManager.detach(user);

        Reservation reservation = ReservationFactory.createReservation(court, user);
        entityManager.persist(reservation);
        entityManager.detach(reservation);

        // Act
        User deletedUser = userRepository.delete(user);

        // Assert
        assertThat(deletedUser.getDeletedAt()).isNotNull();
        Reservation foundReservation = entityManager.find(Reservation.class, reservation.getId());
        assertThat(foundReservation.getDeletedAt()).isNotNull();
    }
}
