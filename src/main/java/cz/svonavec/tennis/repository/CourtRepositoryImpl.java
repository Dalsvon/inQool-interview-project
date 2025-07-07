package cz.svonavec.tennis.repository;

import cz.svonavec.tennis.models.entities.Court;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CourtRepositoryImpl implements CourtRepository{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Court find(long id) {
        Court court = entityManager.find(Court.class, id);
        if (court != null) {
            entityManager.detach(court);
        }
        return court;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Court> findAll() {
        return entityManager.createQuery("SELECT court FROM Court court " +
                        "JOIN FETCH court.surface s WHERE court.deletedAt IS NULL", Court.class)
                .getResultList();
    }

    @Override
    @Transactional
    public Court create(Court court) {
        if (court.getId() == 0) {
            entityManager.persist(court);
            return court;
        }
        return entityManager.merge(court);
    }

    @Override
    @Transactional
    public Court update(Court court) {
        return entityManager.merge(court);
    }

    @Override
    @Transactional
    public Court delete(Court court) {
        LocalDateTime dateTime = LocalDateTime.now();
        entityManager.createQuery("UPDATE Reservation reservation " +
                        "SET reservation.deletedAt = :dateTime WHERE reservation.court.id = :id AND reservation.deletedAt IS NULL")
                .setParameter("id", court.getId()).setParameter("dateTime", dateTime).executeUpdate();
        court.setDeletedAt(dateTime);
        return entityManager.merge(court);
    }
}

