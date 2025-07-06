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
public class CourtRepositoryTests {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CourtRepository courtRepository;

    @BeforeEach
    @Transactional
    void setUp() {
        entityManager.createQuery("DELETE FROM Reservation").executeUpdate();
        entityManager.createQuery("DELETE FROM Court").executeUpdate();
        entityManager.createQuery("DELETE FROM SurfaceType").executeUpdate();
        entityManager.flush();
    }

    @Test
    @Transactional
    void find_courtFound_successfullyReturnedCourt() {
        // Arrange
        SurfaceType surfaceType = SurfaceTypeFactory.createSurfaceType();
        entityManager.persist(surfaceType);

        Court court = CourtFactory.createCourt(surfaceType);
        entityManager.persist(court);
        entityManager.detach(court);

        // Act
        Court foundCourt = courtRepository.find(court.getId());

        // Assert
        assertThat(foundCourt.getId()).isEqualTo(court.getId());
        assertThat(foundCourt.getDescription()).isEqualTo("Standard tennis court");
        assertThat(foundCourt.getSurface().getId()).isEqualTo(surfaceType.getId());
    }

    @Test
    @Transactional
    void find_courtNotFound_successfullyReturnedNull() {
        // Act
        Court foundCourt = courtRepository.find(1L);

        // Assert
        assertThat(foundCourt).isNull();
    }

    @Test
    @Transactional
    void findAll_twoCourtsFound_successfullyReturnedCourts() {
        // Arrange
        SurfaceType surfaceType = SurfaceTypeFactory.createSurfaceType();
        entityManager.persist(surfaceType);

        Court court1 = CourtFactory.createCourt("desc", surfaceType);
        Court court2 = CourtFactory.createCourt(surfaceType);
        entityManager.persist(court1);
        entityManager.persist(court2);

        // Act
        List<Court> foundCourts = courtRepository.findAll();

        // Assert
        assertThat(foundCourts).hasSize(2);
        assertThat(foundCourts.get(0).getDescription()).isEqualTo("desc");
    }

    @Test
    @Transactional
    void findAll_onlyNonDeletedCourts_excludesDeletedCourts() {
        // Arrange
        SurfaceType surfaceType = SurfaceTypeFactory.createSurfaceType();
        entityManager.persist(surfaceType);

        Court activeCourt = CourtFactory.createCourt("Active Court", surfaceType);
        Court deletedCourt = CourtFactory.createCourt("Deleted Court", surfaceType);
        deletedCourt.setDeletedAt(LocalDateTime.now());

        entityManager.persist(activeCourt);
        entityManager.persist(deletedCourt);

        // Act
        List<Court> foundCourts = courtRepository.findAll();

        // Assert
        assertThat(foundCourts).hasSize(1);
        assertThat(foundCourts.get(0).getDescription()).isEqualTo("Active Court");
    }

    @Test
    @Transactional
    void create_newCourt_successfullyCreatedCourt() {
        // Arrange
        SurfaceType surfaceType = SurfaceTypeFactory.createSurfaceType();
        entityManager.persist(surfaceType);

        Court court = CourtFactory.createCourt("New Court", surfaceType);

        // Act
        Court createdCourt = courtRepository.create(court);

        // Assert
        assertThat(createdCourt).isEqualTo(court);
        Court foundCourt = courtRepository.find(createdCourt.getId());
        assertThat(foundCourt).isEqualTo(court);
    }

    @Test
    @Transactional
    void update_courtUpdatedSuccessfully_returnUpdatedCourt() {
        // Arrange
        SurfaceType originalSurface = SurfaceTypeFactory.createSurfaceType();
        SurfaceType newSurface = SurfaceTypeFactory.createSurfaceType();
        newSurface.setName("Clay");
        entityManager.persist(originalSurface);
        entityManager.persist(newSurface);

        Court originalCourt = CourtFactory.createCourt("Original Description", originalSurface);
        entityManager.persist(originalCourt);
        entityManager.detach(originalCourt);

        Court courtUpdate = new Court();
        courtUpdate.setId(originalCourt.getId());
        courtUpdate.setDescription("Updated Description");
        courtUpdate.setSurface(newSurface);
        courtUpdate.setCreatedAt(originalCourt.getCreatedAt());

        // Act
        Court updatedCourt = courtRepository.update(courtUpdate);

        // Assert
        assertThat(updatedCourt.getId()).isEqualTo(originalCourt.getId());
        assertThat(updatedCourt.getDescription()).isEqualTo("Updated Description");
        assertThat(updatedCourt.getSurface().getName()).isEqualTo(newSurface.getName());
        Court foundCourt = courtRepository.find(updatedCourt.getId());
        assertThat(foundCourt.getDescription()).isEqualTo("Updated Description");
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
        Court deletedCourt = courtRepository.delete(court);

        // Assert
        assertThat(deletedCourt.getDeletedAt()).isNotNull();
        Reservation foundReservation = entityManager.find(Reservation.class, reservation.getId());
        assertThat(foundReservation.getDeletedAt()).isNotNull();
    }
}
