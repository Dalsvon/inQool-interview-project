package cz.svonavec.tennis.mapper;

import cz.svonavec.tennis.factory.CourtFactory;
import cz.svonavec.tennis.factory.ReservationFactory;
import cz.svonavec.tennis.factory.SurfaceTypeFactory;
import cz.svonavec.tennis.factory.UserFactory;
import cz.svonavec.tennis.models.dtos.CourtDTO;
import cz.svonavec.tennis.models.dtos.ReservationCreateDTO;
import cz.svonavec.tennis.models.dtos.ReservationDTO;
import cz.svonavec.tennis.models.dtos.SurfaceTypeDTO;
import cz.svonavec.tennis.models.entities.Court;
import cz.svonavec.tennis.models.entities.Reservation;
import cz.svonavec.tennis.models.entities.SurfaceType;
import cz.svonavec.tennis.models.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationMapperTests {
    @Mock
    private CourtMapper courtMapper;

    @InjectMocks
    private ReservationMapper reservationMapper;

    @Test
    void mapToEntity_fromReservationCreateDTO_successfullyMapsToReservationEntity() {
        // Arrange
        LocalDateTime startsAt = LocalDateTime.now().plusDays(2);
        LocalDateTime endsAt = LocalDateTime.now().plusDays(5);

        ReservationCreateDTO createDTO = new ReservationCreateDTO();
        createDTO.setDoubles(true);
        createDTO.setStartsAt(startsAt);
        createDTO.setEndsAt(endsAt);
        createDTO.setCourtId(5L);
        createDTO.setPhoneNumber("+421123456789");

        // Act
        Reservation mappedEntity = reservationMapper.mapToEntity(createDTO);

        // Assert
        assertThat(mappedEntity).isNotNull();
        assertThat(mappedEntity.getId()).isEqualTo(0);
        assertThat(mappedEntity.isDoubles()).isTrue();
        assertThat(mappedEntity.getStartsAt()).isEqualTo(startsAt);
        assertThat(mappedEntity.getEndsAt()).isEqualTo(endsAt);
        assertThat(mappedEntity.getCourt()).isNull();
        assertThat(mappedEntity.getCreatedAt()).isNull();
    }

    @Test
    void mapToDTO_fromReservationEntity_successfullyMapsToReservationDTO() {
        // Arrange
        SurfaceType surfaceType = SurfaceTypeFactory.createSurfaceType();
        surfaceType.setId(1L);

        Court court = CourtFactory.createCourt("Test Court", surfaceType);
        court.setId(1L);

        User user = UserFactory.createUser("+421908123456", "John Smith", "password");
        user.setId(1L);

        LocalDateTime startsAt = LocalDateTime.now().plusDays(2);
        LocalDateTime endsAt = LocalDateTime.now().plusDays(5);
        LocalDateTime createdAt = LocalDateTime.now().minusDays(2);
        LocalDateTime deletedAt = LocalDateTime.now().minusDays(5);

        Reservation reservation = ReservationFactory.createReservation(court, user);
        reservation.setEndsAt(endsAt);
        reservation.setDoubles(true);
        reservation.setStartsAt(startsAt);
        reservation.setId(1L);
        reservation.setCost(BigDecimal.valueOf(25.00));
        reservation.setCreatedAt(createdAt);
        reservation.setDeletedAt(deletedAt);

        CourtDTO courtDTO = new CourtDTO();
        courtDTO.setId(1L);
        courtDTO.setDescription("Test court");

        SurfaceTypeDTO surfaceTypeDTO = new SurfaceTypeDTO();
        surfaceTypeDTO.setId(1L);
        surfaceTypeDTO.setName("Clay");
        surfaceTypeDTO.setCostPerMinute(BigDecimal.valueOf(1.5));
        courtDTO.setSurface(surfaceTypeDTO);

        when(courtMapper.mapToDTO(court)).thenReturn(courtDTO);

        // Act
        ReservationDTO mappedDTO = reservationMapper.mapToDTO(reservation);

        // Assert
        assertThat(mappedDTO).isNotNull();
        assertThat(mappedDTO.getId()).isEqualTo(1L);
        assertThat(mappedDTO.isDoubles()).isTrue();
        assertThat(mappedDTO.getCost()).isEqualTo(BigDecimal.valueOf(25.00));
        assertThat(mappedDTO.getStartsAt()).isEqualTo(startsAt);
        assertThat(mappedDTO.getCreatedAt()).isEqualTo(createdAt);
        assertThat(mappedDTO.getCourt()).isNotNull();
        assertThat(mappedDTO.getCourt().getId()).isEqualTo(1L);
    }

    @Test
    void mapToDTOList_fromReservationEntityList_successfullyMapsToReservationDTOList() {
        // Arrange
        LocalDateTime startsAt = LocalDateTime.now().plusDays(2);
        LocalDateTime endsAt = LocalDateTime.now().plusDays(5);
        SurfaceType surfaceType = SurfaceTypeFactory.createSurfaceType();
        surfaceType.setId(1L);

        Court court1 = CourtFactory.createCourt("court 1", surfaceType);
        court1.setId(1L);

        Court court2 = CourtFactory.createCourt("court 2", surfaceType);
        court2.setId(2L);

        User user = UserFactory.createUser("+421908123456", "John", "pass1");
        user.setId(1L);

        Reservation reservation = ReservationFactory.createReservation(court1, user);
        reservation.setStartsAt(startsAt);
        reservation.setEndsAt(endsAt);
        reservation.setDoubles(true);
        reservation.setId(1L);
        reservation.setCost(BigDecimal.valueOf(25.00));

        Reservation reservation2 = ReservationFactory.createReservation(court2, user);
        reservation2.setStartsAt(startsAt);
        reservation2.setEndsAt(endsAt);
        reservation2.setDoubles(true);
        reservation2.setId(2L);
        reservation2.setCost(BigDecimal.valueOf(180.00));

        List<Reservation> reservations = List.of(reservation, reservation2);

        CourtDTO courtDTO1 = new CourtDTO();
        courtDTO1.setId(1L);
        courtDTO1.setDescription("court 1");

        CourtDTO courtDTO2 = new CourtDTO();
        courtDTO2.setId(2L);
        courtDTO2.setDescription("court 2");

        when(courtMapper.mapToDTO(court1)).thenReturn(courtDTO1);
        when(courtMapper.mapToDTO(court2)).thenReturn(courtDTO2);

        // Act
        List<ReservationDTO> mappedDTOs = reservationMapper.mapToDTOList(reservations);

        // Assert
        assertThat(mappedDTOs).isNotNull();
        assertThat(mappedDTOs.size()).isEqualTo(2);

        // Verify first reservation
        ReservationDTO dto1 = mappedDTOs.get(0);
        assertThat(dto1.getId()).isEqualTo(1L);
        assertThat(dto1.getCost()).isEqualTo(BigDecimal.valueOf(25.00));
        assertThat(dto1.getStartsAt()).isEqualTo(startsAt);
        assertThat(dto1.getCourt().getId()).isEqualTo(1L);
        assertThat(dto1.getUserId()).isEqualTo(1L);
    }
}
