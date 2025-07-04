package cz.svonavec.tennis.repository;

import cz.svonavec.tennis.models.entities.Reservation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Reservation find(long id) {
        Reservation reservation = entityManager.find(Reservation.class, id);
        if (reservation != null) {
            entityManager.detach(reservation);
        }
        return reservation;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return entityManager.createQuery("SELECT reservation FROM Reservation reservation WHERE reservation.deletedAt IS NULL", Reservation.class)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByCourt(long id) {
        return entityManager.createQuery("SELECT reservation FROM Reservation reservation " +
                        "WHERE reservation.deletedAt IS NULL AND reservation.court.id = :id " +
                        "ORDER BY reservation.createdAt ASC", Reservation.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findByPhone(String phoneNumber, boolean futureOnly) {
        LocalDateTime time = LocalDateTime.now();
        if (futureOnly) {
            return entityManager.createQuery("SELECT reservation FROM Reservation reservation " +
                            "WHERE reservation.deletedAt IS NULL AND reservation.user.phoneNumber = :phoneNumber AND " +
                            "reservation.startsAt < :time ORDER BY reservation.createdAt ASC", Reservation.class)
                    .setParameter("phoneNumber", phoneNumber)
                    .setParameter("time", time)
                    .getResultList();
        }
        return entityManager.createQuery("SELECT reservation FROM Reservation reservation " +
                        "WHERE reservation.deletedAt IS NULL AND reservation.user.phoneNumber = :phoneNumber " +
                        "ORDER BY reservation.createdAt ASC", Reservation.class)
                .setParameter("phoneNumber", phoneNumber)
                .getResultList();
    }

    @Override
    @Transactional
    public Reservation create(Reservation reservation) {
        if (reservation.getId() == 0) {
            entityManager.persist(reservation);
            return reservation;
        }
        return entityManager.merge(reservation);
    }

    @Override
    @Transactional
    public Reservation update(Reservation reservation) {
        return entityManager.merge(reservation);
    }

    @Override
    @Transactional
    public Reservation delete(Reservation reservation) {
        LocalDateTime dateTime = LocalDateTime.now();
        reservation.setDeletedAt(dateTime);
        return entityManager.merge(reservation);
    }
}
