package cz.svonavec.tennis.service;

import cz.svonavec.tennis.exception.BadRequestException;
import cz.svonavec.tennis.exception.ResourceNotFoundException;
import cz.svonavec.tennis.factory.CourtFactory;
import cz.svonavec.tennis.factory.ReservationFactory;
import cz.svonavec.tennis.factory.SurfaceTypeFactory;
import cz.svonavec.tennis.factory.UserFactory;
import cz.svonavec.tennis.models.entities.Court;
import cz.svonavec.tennis.models.entities.Reservation;
import cz.svonavec.tennis.models.entities.SurfaceType;
import cz.svonavec.tennis.models.entities.User;
import cz.svonavec.tennis.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTests {
    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CourtService courtService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void find_reservationFound_returnsReservation() {
        // Arrange
        Reservation createdReservation = ReservationFactory.createReservation();
        when(reservationRepository.find(1L)).thenReturn(createdReservation);

        // Act
        Reservation reservation = reservationService.findById(1L);

        // Assert
        assertThat(reservation).isEqualTo(ReservationFactory.createReservation());
        assertThat(reservation.getCost()).isEqualTo(createdReservation.getCost());
    }

    @Test
    void find_reservationNotFound_throwsResourceNotFoundException() {
        assertThrows(ResourceNotFoundException.class, () -> reservationService.findById(1));
    }

    @Test
    void findAll_twoReservationsFound_returnsReservations() {
        Court court = CourtFactory.createCourt();
        court.setDescription("Other court");

        List<Reservation> reservations = List.of(ReservationFactory.createReservation(),
                ReservationFactory.createReservation(court, UserFactory.createUser()));
        when(reservationRepository.findAll()).thenReturn(reservations);

        // Act
        List<Reservation> reservationsFound = reservationService.findAll();

        // Assert
        assertThat(reservationsFound.size()).isEqualTo(2);
        assertThat(reservationsFound.getFirst().getCost()).isEqualTo(BigDecimal.valueOf(60.00));
        assertThat(reservationsFound.get(1).getCourt().getDescription()).isEqualTo("Other court");
    }

    @Test
    void findByPhone_twoReservationsFound_returnsReservations() {
        Court court = CourtFactory.createCourt();
        court.setDescription("Other court");
        court.setId(1);

        List<Reservation> reservations = List.of(ReservationFactory.createReservation(),
                ReservationFactory.createReservation(court, UserFactory.createUser()));
        when(reservationRepository.findByPhone("+421123456789", false)).thenReturn(reservations);

        // Act
        List<Reservation> reservationsFound = reservationService.findByPhone("+421123456789", false);

        // Assert
        assertThat(reservationsFound.size()).isEqualTo(2);
        assertThat(reservationsFound.getFirst().getCost()).isEqualTo(BigDecimal.valueOf(60.00));
        assertThat(reservationsFound.get(1).getCourt().getDescription()).isEqualTo("Other court");
    }

    @Test
    void findByCourt_twoReservationsFound_returnsReservations() {
        User user = UserFactory.createUser();
        user.setId(1L);
        Court court = CourtFactory.createCourt();
        court.setDescription("Other court");
        court.setId(1L);

        List<Reservation> reservations = List.of(ReservationFactory.createReservation(CourtFactory.createCourt(), user),
                ReservationFactory.createReservation(court, user));
        when(reservationRepository.findByCourt(1L)).thenReturn(reservations);

        // Act
        List<Reservation> reservationsFound = reservationService.findByCourt(1L);

        // Assert
        assertThat(reservationsFound.size()).isEqualTo(2);
        assertThat(reservationsFound.getFirst().getCost()).isEqualTo(BigDecimal.valueOf(60.00));
        assertThat(reservationsFound.get(1).getCourt().getDescription()).isEqualTo("Other court");
    }

    @Test
    void create_reservationCreated_returnsReservation() {
        // Arrange
        Reservation reservation = ReservationFactory.createReservation();
        reservation.setId(0L);
        reservation.setCost(BigDecimal.ZERO);
        reservation.setCourt(null);
        reservation.setUser(null);
        when(reservationRepository.create(reservation)).thenReturn(ReservationFactory.createReservation());
        when(courtService.findById(1L)).thenReturn(CourtFactory.createCourt());
        when(userService.findByPhoneNumber("+421123456789")).thenReturn(UserFactory.createUser());
        when(reservationRepository.findByCourt(1L)).thenReturn(List.of());

        // Act
        BigDecimal cost = reservationService.create(reservation, "+421123456789", 1L);

        // Assert
        assertThat(cost).isEqualTo(BigDecimal.valueOf(60.00));
        verify(reservationRepository).create(reservation);
        verify(courtService).findById(1L);
        verify(reservationRepository).findByCourt(1L);
    }

    @Test
    void create_reservationOverlapping_throwsthrowsBadRequestException() {
        // Arrange
        Reservation reservation = ReservationFactory.createReservation();
        reservation.setId(0L);
        reservation.setCost(BigDecimal.ZERO);
        reservation.setCourt(null);
        reservation.setUser(null);
        Reservation conflict = ReservationFactory.createReservation();
        conflict.setId(1L);
        when(reservationRepository.findByCourt(1L)).thenReturn(List.of(conflict));

        // Act
        assertThrows(BadRequestException.class, () -> reservationService.create(reservation, "+421908123456", 1L));
    }

    @Test
    void create_reservationHasId_throwsthrowsBadRequestException() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        assertThrows(BadRequestException.class, () -> reservationService.create(reservation, "+421908123456", 1L));
    }

    @Test
    void create_reservationBadEndsBeforeStarts_throwsthrowsBadRequestException() {
        Reservation reservation = ReservationFactory.createReservation();
        reservation.setStartsAt(reservation.getEndsAt().plusDays(2));
        assertThrows(BadRequestException.class, () -> reservationService.create(reservation, "+421908123456", 1L));
    }

    @Test
    void update_reservationUpdated_returnsCourt() {
        // Arrange
        when(reservationRepository.findByCourt(0L)).thenReturn(List.of());
        Reservation foundReservation = ReservationFactory.createReservation();
        foundReservation.setId(1L);
        Reservation update = ReservationFactory.createReservation();
        update.setId(1);
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        update.setStartsAt(start);
        update.setEndsAt(end);
        update.setDoubles(true);
        update.setCost(BigDecimal.ONE);
        when(reservationRepository.find(1L)).thenReturn(foundReservation);
        when(reservationRepository.update(any(Reservation.class))).thenReturn(update);

        // Act
        Reservation reservation = reservationService.update(1L, start, end, true, BigDecimal.ONE);

        // Assert
        assertThat(reservation).isEqualTo(update);
        assertThat(reservation.getCost()).isEqualTo(BigDecimal.ONE);
    }

    @Test
    void update_noUpdateFields_throwsBadRequestException() {
        // Arrange
        when(reservationRepository.find(1L)).thenReturn(ReservationFactory.createReservation());

        // Act
        assertThrows(BadRequestException.class, () -> reservationService.update(1L, null, null, null, null));
    }

    @Test
    void delete_reservationDeleted_returnsReservation() {
        // Arrange
        Reservation reservationDeleted = ReservationFactory.createReservation();
        reservationDeleted.setId(1L);
        reservationDeleted.setDeletedAt(LocalDateTime.now());

        when(reservationRepository.find(1L)).thenReturn(ReservationFactory.createReservation());
        when(reservationRepository.delete(ReservationFactory.createReservation())).thenReturn(reservationDeleted);

        // Act
        Reservation reservation = reservationService.delete(1L);

        // Assert
        assertThat(reservation.getDeletedAt()).isNotNull();
        assertThat(reservation.getId()).isEqualTo(1L);
        verify(reservationRepository).find(1L);
        verify(reservationRepository).delete(ReservationFactory.createReservation());
    }
}
