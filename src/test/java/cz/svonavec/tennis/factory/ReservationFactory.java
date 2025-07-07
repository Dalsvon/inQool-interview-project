package cz.svonavec.tennis.factory;

import cz.svonavec.tennis.models.dtos.*;
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

    public static Reservation createReservation() {
        Reservation reservation = new Reservation();
        reservation.setDoubles(false);
        reservation.setStartsAt(LocalDateTime.of(2025, 1, 1, 10, 0));
        reservation.setEndsAt(LocalDateTime.of(2025, 1, 1, 11, 0));
        reservation.setCost(BigDecimal.valueOf(60.00));
        reservation.setCourt(CourtFactory.createCourt());
        reservation.setUser(UserFactory.createUser());
        reservation.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));
        return reservation;
    }

    public static ReservationDTO createReservationDTO() {
        ReservationDTO reservation = new ReservationDTO();
        reservation.setDoubles(false);
        reservation.setStartsAt(LocalDateTime.of(2025, 1, 1, 10, 0));
        reservation.setEndsAt(LocalDateTime.of(2025, 1, 1, 11, 0));
        reservation.setCost(BigDecimal.valueOf(60.00));
        reservation.setCourt(CourtFactory.createCourtDTO());
        reservation.setUserId(1);
        reservation.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));
        return reservation;
    }

    public static ReservationDTO createReservationDTORest() {
        ReservationDTO reservation = new ReservationDTO();
        reservation.setDoubles(false);
        reservation.setId(1L);
        reservation.setStartsAt(LocalDateTime.of(2025, 1, 1, 10, 0));
        reservation.setEndsAt(LocalDateTime.of(2025, 1, 1, 11, 0));
        reservation.setCost(BigDecimal.valueOf(60.00));
        reservation.setCourt(CourtFactory.createCourtDTORest());
        reservation.setUserId(1L);
        reservation.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));
        return reservation;
    }

    public static ReservationCreateDTO createReservationCreateDTO() {
        ReservationCreateDTO reservationCreateDTO = new ReservationCreateDTO();
        reservationCreateDTO.setDoubles(false);
        reservationCreateDTO.setStartsAt(LocalDateTime.now().plusHours(1));
        reservationCreateDTO.setEndsAt(LocalDateTime.now().plusHours(2));
        reservationCreateDTO.setCourtId(1L);
        reservationCreateDTO.setPhoneNumber("+420907123456");
        return reservationCreateDTO;
    }

    public static ReservationUpdateDTO createReservationUpdateDTO() {
        ReservationUpdateDTO reservationUpdateDTO = new ReservationUpdateDTO();
        reservationUpdateDTO.setId(1L);
        reservationUpdateDTO.setDoubles(true);
        reservationUpdateDTO.setCost(new BigDecimal("200.00"));
        return reservationUpdateDTO;
    }
}
