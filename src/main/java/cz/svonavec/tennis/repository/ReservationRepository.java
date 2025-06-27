package cz.svonavec.tennis.repository;

import cz.svonavec.tennis.models.entities.Reservation;

import java.util.List;

public interface ReservationRepository {
    Reservation find(long id);
    List<Reservation> findAll();
    List<Reservation> findByCourt(long courtId);
    List<Reservation> findByPhone(String phoneNumber, boolean futureOnly);
    Reservation update(Reservation reservation);
    Reservation create(Reservation reservation);
    Reservation delete(Reservation reservation);
}
