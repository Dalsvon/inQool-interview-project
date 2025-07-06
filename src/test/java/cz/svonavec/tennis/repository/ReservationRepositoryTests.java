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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class ReservationRepositoryTests {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ReservationRepository reservationRepository;

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
    void find_reservationFound_successfullyReturnedReservation() {
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
        Reservation reservationFound = reservationRepository.find(reservation.getId());

        // Assert
        assertThat(reservationFound.getId()).isEqualTo(reservation.getId());
        assertThat(reservationFound.getCourt().getId()).isEqualTo(court.getId());
        assertThat(reservationFound.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @Transactional
    void find_surfaceNotFound_successfullyReturnedNull() {
        // Act
        Reservation reservation = reservationRepository.find(1L);

        // Assert
        assertThat(reservation).isEqualTo(null);
    }

    @Test
    @Transactional
    void findAll_twoSurfacesFound_successfullyReturnedReservation() {
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
        Reservation reservation2 = ReservationFactory.createReservation(court, user);
        entityManager.persist(reservation2);
        entityManager.detach(reservation2);

        // Act
        List<Reservation> foundReservations = reservationRepository.findAll();

        // Assert
        assertThat(foundReservations.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    void findByCourt_reservationsFound_successfullyReturnedReservation() {
        // Arrange
        SurfaceType surfaceType = SurfaceTypeFactory.createSurfaceType();
        entityManager.persist(surfaceType);

        Court courtNotUsed = CourtFactory.createCourt(surfaceType);
        entityManager.persist(courtNotUsed);
        entityManager.detach(courtNotUsed);

        Court court = CourtFactory.createCourt(surfaceType);
        entityManager.persist(court);
        entityManager.detach(court);

        User user = UserFactory.createUser();
        entityManager.persist(user);
        entityManager.detach(user);

        Reservation reservation = ReservationFactory.createReservation(court, user);
        entityManager.persist(reservation);
        entityManager.detach(reservation);
        Reservation reservation2 = ReservationFactory.createReservation(courtNotUsed, user);
        entityManager.persist(reservation2);
        entityManager.detach(reservation2);

        // Act
        List<Reservation> foundReservations = reservationRepository.findByCourt(court.getId());

        // Assert
        assertThat(foundReservations.size()).isEqualTo(1);
        assertThat(foundReservations.get(0).getId()).isEqualTo(reservation.getId());
    }

    @Test
    @Transactional
    void findByUserAll_reservationsFound_successfullyReturnedReservation() {
        // Arrange
        SurfaceType surfaceType = SurfaceTypeFactory.createSurfaceType();
        entityManager.persist(surfaceType);

        Court court = CourtFactory.createCourt(surfaceType);
        entityManager.persist(court);
        entityManager.detach(court);

        User user = UserFactory.createUser();
        entityManager.persist(user);
        entityManager.detach(user);

        User adminUser = UserFactory.createAdminUser();
        entityManager.persist(adminUser);
        entityManager.detach(adminUser);

        Reservation reservation = ReservationFactory.createReservation(court, user);
        entityManager.persist(reservation);
        entityManager.detach(reservation);
        Reservation reservation2 = ReservationFactory.createReservation(court, adminUser);
        entityManager.persist(reservation2);
        entityManager.detach(reservation2);

        // Act
        List<Reservation> foundReservations = reservationRepository.findByPhone(user.getPhoneNumber(), false);

        // Assert
        assertThat(foundReservations.size()).isEqualTo(1);
        assertThat(foundReservations.get(0).getId()).isEqualTo(reservation.getId());
    }

    @Test
    @Transactional
    void findByUserFutureOnly_reservationsFound_successfullyReturnedReservation() {
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
        reservation.setStartsAt(LocalDateTime.now().minusDays(5));
        entityManager.persist(reservation);
        entityManager.detach(reservation);
        Reservation reservation2 = ReservationFactory.createReservation(court, user);
        reservation2.setStartsAt(LocalDateTime.now().plusDays(5));
        entityManager.persist(reservation2);
        entityManager.detach(reservation2);

        // Act
        List<Reservation> foundReservations = reservationRepository.findByPhone(user.getPhoneNumber(), true);

        // Assert
        assertThat(foundReservations.size()).isEqualTo(1);
        assertThat(foundReservations.get(0).getId()).isEqualTo(reservation2.getId());
    }

    @Test
    @Transactional
    void create_newReservationCreated_successfullyReturnedCreatedReservation() {
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

        // Act
        Reservation reservationCreated = reservationRepository.create(reservation);

        // Assert
        assertThat(reservationCreated).isEqualTo(reservation);
        Reservation reservation1 = reservationRepository.find(reservationCreated.getId());
        assertThat(reservation1).isEqualTo(reservationCreated);
    }

    @Test
    @Transactional
    void update_reservationUpdatedSuccessfully_returnUpdatedReservation() {
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

        LocalDateTime date = LocalDateTime.now().plusDays(2);

        Reservation reservationUpdate = ReservationFactory.createReservation(court, user);
        reservationUpdate.setId(reservation.getId());
        reservationUpdate.setStartsAt(date);
        reservationUpdate.setEndsAt(LocalDateTime.now().plusDays(10));

        // Act
        Reservation reservationUpdated = reservationRepository.update(reservationUpdate);

        // Assert
        assertThat(reservationUpdated.getId()).isEqualTo(reservation.getId());
        assertThat(reservationUpdated).isEqualTo(reservationUpdate);
        Reservation reservation1 = reservationRepository.find(reservationUpdated.getId());
        assertThat(reservation1.getStartsAt()).isEqualTo(date);
    }

    @Test
    @Transactional
    void delete_reservationDeletedSuccessfully_returnDeletedReservation() {
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
        Reservation reservationDeleted = reservationRepository.delete(reservation);

        // Assert
        assertThat(reservationDeleted.getId()).isEqualTo(reservation.getId());
        assertThat(reservationDeleted).isEqualTo(reservation);
        Reservation reservation1 = reservationRepository.find(reservationDeleted.getId());
        assertThat(reservation1.getDeletedAt()).isNotNull();
    }
}
