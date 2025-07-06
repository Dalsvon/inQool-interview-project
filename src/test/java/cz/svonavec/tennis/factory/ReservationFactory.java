package cz.svonavec.tennis.factory;

import cz.svonavec.tennis.models.dtos.CourtDTO;
import cz.svonavec.tennis.models.dtos.ReservationDTO;
import cz.svonavec.tennis.models.dtos.UserDTO;
import cz.svonavec.tennis.models.entities.Court;
import cz.svonavec.tennis.models.entities.Reservation;
import cz.svonavec.tennis.models.entities.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static cz.svonavec.tennis.factory.CourtFactory.createCourt;
import static cz.svonavec.tennis.factory.CourtFactory.createCourtDTO;
import static cz.svonavec.tennis.factory.UserFactory.createUser;

public class ReservationFactory {
    public static Reservation createReservation(Court court, User user) {
        Reservation reservation = new Reservation();
        reservation.setDoubles(false);
        reservation.setStartsAt(LocalDateTime.of(2025, 1, 1, 10, 0));
        reservation.setEndsAt(LocalDateTime.of(2025, 1, 1, 11, 0));
        reservation.setCost(BigDecimal.valueOf(60.00));
        reservation.setCourt(court);
        reservation.setUser(user);
        reservation.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));
        return reservation;
    }

    public static ReservationDTO createReservationDTO(CourtDTO courtDTO) {
        ReservationDTO reservation = new ReservationDTO();
        reservation.setDoubles(false);
        reservation.setStartsAt(LocalDateTime.of(2025, 1, 1, 10, 0));
        reservation.setEndsAt(LocalDateTime.of(2025, 1, 1, 11, 0));
        reservation.setCost(BigDecimal.valueOf(60.00));
        reservation.setCourt(courtDTO);
        reservation.setUserId(1);
        reservation.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));
        return reservation;
    }
}
