package cz.svonavec.tennis.repository;

import cz.svonavec.tennis.models.entities.Reservation;

import java.util.List;

public interface ReservationRepository {
    /**
     * Finds and returns reservation from the database with corresponding id
     *
     * @param id id of the reservation
     * @return found reservation
     */
    Reservation find(long id);

    /**
     * Finds and returns all reservations in the database (undeleted)
     *
     * @return all reservations
     */
    List<Reservation> findAll();

    /**
     * Finds and returns all reservations from the database for given court
     *
     * @param courtId id of the court
     * @return all reservations for given court
     */
    List<Reservation> findByCourt(long courtId);

    /**
     * Finds and returns all reservations from the database for given user identified by unique phone number
     *
     * @param phoneNumber unique phone number of the user
     * @param futureOnly true, if only reservations starting in future are requested
     * @return all reservations for given user
     */
    List<Reservation> findByPhone(String phoneNumber, boolean futureOnly);

    /**
     * Updates the reservation with given data (changes information in reservation with the same id) in the database
     *
     * @param reservation reservation data
     * @return updated reservation
     */
    Reservation update(Reservation reservation);

    /**
     * Creates and saves reservation from given data
     *
     * @param reservation reservation data
     * @return created reservation
     */
    Reservation create(Reservation reservation);

    /**
     * Deletes reservation from the database. Performed as SOFT delete by assigning deletedAt field
     *
     * @param reservation reservation data
     * @return deleted reservation
     */
    Reservation delete(Reservation reservation);
}
