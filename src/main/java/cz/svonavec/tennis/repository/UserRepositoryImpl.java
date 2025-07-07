package cz.svonavec.tennis.repository;

import cz.svonavec.tennis.models.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public User find(long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.detach(user);
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User findByPhoneNumber(String phoneNumber) {
        User user;
        try {
            user = entityManager.createQuery("SELECT user FROM User user " +
                            "WHERE user.phoneNumber = :phoneNumber", User.class)
                    .setParameter("phoneNumber", phoneNumber)
                    .getSingleResult();
        } catch (NoResultException e) {
            user = null;
        }
        if (user != null) {
            entityManager.detach(user);
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return entityManager.createQuery("SELECT user FROM User user WHERE user.deletedAt IS NULL", User.class)
                .getResultList();
    }

    @Override
    @Transactional
    public User register(User user) {
        if (user.getId() == 0) {
            entityManager.persist(user);
            return user;
        }
        return entityManager.merge(user);
    }

    @Override
    @Transactional
    public User update(User user) {
        return entityManager.merge(user);
    }

    @Override
    @Transactional
    public User delete(User user) {
        LocalDateTime dateTime = LocalDateTime.now();
        entityManager.createQuery("UPDATE Reservation reservation " +
                        "SET reservation.deletedAt = :dateTime WHERE reservation.user.id = :id AND reservation.deletedAt IS NULL")
                .setParameter("id", user.getId()).setParameter("dateTime", dateTime).executeUpdate();
        user.setDeletedAt(dateTime);
        return entityManager.merge(user);
    }
}
