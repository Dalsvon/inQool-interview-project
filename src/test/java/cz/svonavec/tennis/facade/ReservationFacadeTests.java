package cz.svonavec.tennis.facade;

import cz.svonavec.tennis.factory.ReservationFactory;
import cz.svonavec.tennis.mapper.ReservationMapper;
import cz.svonavec.tennis.models.dtos.ReservationCreateDTO;
import cz.svonavec.tennis.models.dtos.ReservationDTO;
import cz.svonavec.tennis.models.dtos.ReservationUpdateDTO;
import cz.svonavec.tennis.models.entities.Reservation;
import cz.svonavec.tennis.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationFacadeTests {
    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationFacade reservationFacade;

    @Test
    void findById_findsReservation_returnsReservation() {
        // Arrange
        when(reservationService.findById(1L)).thenReturn(ReservationFactory.createReservation());
        when(reservationMapper.mapToDTO(ReservationFactory.createReservation())).thenReturn(ReservationFactory.createReservationDTO());

        // Act
        ReservationDTO reservationDTO = reservationFacade.findById(1L);

        // Assert
        verify(reservationService).findById(1);
        assertThat(reservationDTO).isEqualTo(ReservationFactory.createReservationDTO());
    }

    @Test
    void findAll_findsAllReservations_returnsReservations() {
        // Arrange
        List<Reservation> reservations = List.of(ReservationFactory.createReservation(), ReservationFactory.createReservation());
        List<ReservationDTO> reservationDTOS = List.of(ReservationFactory.createReservationDTO(), ReservationFactory.createReservationDTO());
        when(reservationService.findAll()).thenReturn(reservations);
        when(reservationMapper.mapToDTOList(reservations)).thenReturn(reservationDTOS);

        // Act
        List<ReservationDTO> result = reservationFacade.findAll();

        // Assert
        verify(reservationService).findAll();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void findByPhoneNumber_findsAllReservations_returnsReservations() {
        // Arrange
        List<Reservation> reservations = List.of(ReservationFactory.createReservation(), ReservationFactory.createReservation());
        List<ReservationDTO> reservationDTOS = List.of(ReservationFactory.createReservationDTO(), ReservationFactory.createReservationDTO());
        when(reservationService.findByPhone("+421123456789", true)).thenReturn(reservations);
        when(reservationMapper.mapToDTOList(reservations)).thenReturn(reservationDTOS);

        // Act
        List<ReservationDTO> result = reservationFacade.findByPhone("+421123456789", true);

        // Assert
        verify(reservationService).findByPhone("+421123456789", true);
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void findByCourt_findsAllReservations_returnsReservations() {
        // Arrange
        List<Reservation> reservations = List.of(ReservationFactory.createReservation(), ReservationFactory.createReservation());
        List<ReservationDTO> reservationDTOS = List.of(ReservationFactory.createReservationDTO(), ReservationFactory.createReservationDTO());
        when(reservationService.findByCourt(1L)).thenReturn(reservations);
        when(reservationMapper.mapToDTOList(reservations)).thenReturn(reservationDTOS);

        // Act
        List<ReservationDTO> result = reservationFacade.findByCourt(1L);

        // Assert
        verify(reservationService).findByCourt(1L);
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void create_createsReservation_returnsReservation() {
        // Arrange
        Reservation reservation = ReservationFactory.createReservation();
        ReservationCreateDTO reservationCreateDTO = new ReservationCreateDTO();
        reservationCreateDTO.setDoubles(reservation.isDoubles());
        reservationCreateDTO.setCourtId(reservation.getCourt().getId());
        reservationCreateDTO.setEndsAt(reservation.getEndsAt());
        reservationCreateDTO.setStartsAt(reservation.getStartsAt());
        reservationCreateDTO.setPhoneNumber(reservation.getUser().getPhoneNumber());

        when(reservationService.create(ReservationFactory.createReservation(), reservationCreateDTO.getPhoneNumber(),
                reservationCreateDTO.getCourtId())).thenReturn(ReservationFactory.createReservation().getCost());
        when(reservationMapper.mapToEntity(reservationCreateDTO)).thenReturn(reservation);

        // Act
        BigDecimal cost = reservationFacade.create(reservationCreateDTO);

        // Assert
        verify(reservationService).create(reservation, reservationCreateDTO.getPhoneNumber(), reservationCreateDTO.getCourtId());
        assertThat(cost).isEqualTo(reservation.getCost());
    }

    @Test
    void update_updatesReservation_returnsReservation() {
        // Arrange
        ReservationUpdateDTO reservationUpdateDTO = new ReservationUpdateDTO();
        reservationUpdateDTO.setDoubles(true);
        reservationUpdateDTO.setId(1L);
        when(reservationService.update(1L, null, null, true, null)).thenReturn(ReservationFactory.createReservation());
        when(reservationMapper.mapToDTO(ReservationFactory.createReservation())).thenReturn(ReservationFactory.createReservationDTO());

        // Act
        ReservationDTO reservationDTO = reservationFacade.update(reservationUpdateDTO);

        // Assert
        verify(reservationService).update(1L, null, null, true, null);
        assertThat(reservationDTO).isEqualTo(ReservationFactory.createReservationDTO());
    }

    @Test
    void delete_deleteReservation_returnsReservation() {
        // Arrange
        LocalDateTime time = LocalDateTime.now();
        Reservation reservation = ReservationFactory.createReservation();
        reservation.setDeletedAt(time);
        when(reservationService.delete(1L)).thenReturn(reservation);
        ReservationDTO reservationDTO = ReservationFactory.createReservationDTO();
        reservationDTO.setDeletedAt(time);
        when(reservationMapper.mapToDTO(reservation)).thenReturn(reservationDTO);

        // Act
        ReservationDTO reservationDTODeleted = reservationFacade.delete(1L);

        // Assert
        verify(reservationService).delete(1L);
        assertThat(reservationDTODeleted.getDeletedAt()).isNotNull();
        assertThat(reservationDTODeleted).isEqualTo(reservationDTO);
    }
}
