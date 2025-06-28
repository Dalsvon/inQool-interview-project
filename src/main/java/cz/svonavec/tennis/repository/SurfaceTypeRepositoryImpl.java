package cz.svonavec.tennis.repository;

import cz.svonavec.tennis.models.entities.SurfaceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class SurfaceTypeRepositoryImpl implements SurfaceTypeRepository{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public SurfaceType find(long id) {
        SurfaceType type = entityManager.find(SurfaceType.class, id);
        entityManager.detach(type);
        return type;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SurfaceType> findAll() {
        return entityManager.createQuery("SELECT surfaceType FROM SurfaceType surfaceType", SurfaceType.class)
                .getResultList();
    }

    @Override
    @Transactional
    public SurfaceType create(SurfaceType surfaceType) {
        if (surfaceType.getId() == 0) {
            entityManager.persist(surfaceType);
            return surfaceType;
        }
        return entityManager.merge(surfaceType);
    }

    @Override
    @Transactional
    public SurfaceType update(SurfaceType surfaceType) {
        return entityManager.merge(surfaceType);
    }

    @Override
    @Transactional
    public SurfaceType delete(SurfaceType surfaceType) {
        LocalDateTime dateTime = LocalDateTime.now();
        surfaceType.setDeletedAt(dateTime);
        return entityManager.merge(surfaceType);
    }
}
