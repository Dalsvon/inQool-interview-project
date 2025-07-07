package cz.svonavec.tennis.service;

import cz.svonavec.tennis.exception.BadRequestException;
import cz.svonavec.tennis.exception.ResourceNotFoundException;
import cz.svonavec.tennis.models.entities.Court;
import cz.svonavec.tennis.models.entities.Reservation;
import cz.svonavec.tennis.models.entities.Role;
import cz.svonavec.tennis.models.entities.User;
import cz.svonavec.tennis.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
    public final ReservationRepository reservationRepository;

    public final UserService userService;
    public final CourtService courtService;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              UserService userService,
                              CourtService courtService){
        this.reservationRepository = reservationRepository;
        this.userService = userService;
        this.courtService = courtService;
    }

    @Transactional(readOnly = true)
    public Reservation findById(long id) {
        Reservation reservation = reservationRepository.find(id);
        if (reservation == null || reservation.getDeletedAt() != null) {
            throw new ResourceNotFoundException("Couldn't find reservation with this id.");
        }
        return reservation;
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Reservation> findByCourt(long id) {
        return reservationRepository.findByCourt(id);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findByPhone(String phoneNumber, boolean futureOnly) {
        return reservationRepository.findByPhone(phoneNumber, futureOnly);
    }

    /**
     * Checks if a time duration of a reservation (consisting of 2 timestamps) is in conflict (is overlapping)
     * with another reservation on the same court. Two reservations can overlap in one point
     * (one can start when another ends), but otherwise they must be completely non-overlapping.
     *
     * During update the reservation can overlap with itself.
     *
     * @param start start of the reservation, which must be before end of the reservation
     * @param end end of the reservation
     * @param courtId id of the court for reservation
     * @param id id of the reservation
     * @return true, if there is a conflict between 2 reservations for the same court
     */
    @Transactional
    public boolean isOverlapping(LocalDateTime start, LocalDateTime end, long courtId, long id) {
        if (end.isBefore(start)) {
            throw new BadRequestException("Reservation start date must be before end date.");
        }
        List<Reservation> reservations = reservationRepository.findByCourt(courtId);
        for (Reservation res : reservations) {
            // Reservation can begin at the same time another one ends
            if (!(res.getStartsAt().isAfter(end) || start.isAfter(res.getEndsAt()) ||
                    res.getStartsAt().isEqual(end) || start.isEqual(res.getEndsAt())) && res.getId() != id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a new reservation for a user with unique phone number and a court. Reservations for one court cannot
     * overlap and startsAt timestamp must be before endsAt timestamp. User also has to have User Role to be allowed to
     * create a reservation. After all conditions are checked, cost of the reservation is calculated and reservation is
     * created
     *
     * @param reservation reservation data
     * @param phoneNumber unique phone number
     * @param courtId unique court id
     * @return overall cost of the reservation
     */
    @Transactional
    public BigDecimal create(Reservation reservation, String phoneNumber, long courtId) {
        if (reservation.getId() != 0) {
            throw new BadRequestException("Trying to create a court with set id.");
        }
        if (isOverlapping(reservation.getStartsAt(), reservation.getEndsAt(), courtId, 0)) {
            throw new BadRequestException("There already exists a reservation for this court overlapping with this reservation.");
        }
        User user = userService.findByPhoneNumber(phoneNumber);
        if (!user.getRoles().contains(Role.USER)) {
            throw new BadRequestException("This user cannot reserve courts.");
        }
        Court court = courtService.findById(courtId);
        reservation.setUser(user);
        reservation.setCourt(court);
        reservation.calculateCost();
        return reservationRepository.create(reservation).getCost();
    }

    @Transactional
    public Reservation update(long id, LocalDateTime start, LocalDateTime end, Boolean doubles, BigDecimal cost) {
        Reservation foundReservation = findById(id);
        if (start != null || end != null || doubles != null || cost != null) {
            if (start != null) {
                foundReservation.setStartsAt(start);
            }
            if (end != null) {
                foundReservation.setEndsAt(end);
            }
            if (isOverlapping(foundReservation.getStartsAt(), foundReservation.getEndsAt(), foundReservation.getCourt().getId(), foundReservation.getId())) {
                throw new BadRequestException("There already exists a reservation for this court overlapping with this reservation.");
            }
            if (doubles != null) {
                foundReservation.setDoubles(doubles);
            }
            foundReservation.calculateCost();
            if (cost != null) {
                foundReservation.setCost(cost);
            }
        } else {
            throw new BadRequestException("At least one query field must be used.");
        }
        return reservationRepository.update(foundReservation);
    }

    @Transactional
    public Reservation delete(long id) {
        Reservation reservation = findById(id);
        return reservationRepository.delete(reservation);
    }
}
