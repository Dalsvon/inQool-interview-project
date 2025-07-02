package cz.svonavec.tennis.repository;

import cz.svonavec.tennis.factory.SurfaceTypeFactory;
import cz.svonavec.tennis.models.entities.SurfaceType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class SurfaceTypeRepositoryTests {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SurfaceTypeRepository surfaceTypeRepository;

    @BeforeEach
    @Transactional
    void setUp() {
        entityManager.createQuery("DELETE FROM SurfaceType").executeUpdate();
        entityManager.flush();
    }

    @Test
    @Transactional
    void find_surfaceFound_successfullyReturnedSurfaceType() {
        // Arrange
        SurfaceType surfaceType = SurfaceTypeFactory.createSurfaceType();
        entityManager.persist(surfaceType);
        entityManager.detach(surfaceType);

        // Act
        SurfaceType foundSurface = surfaceTypeRepository.find(surfaceType.getId());

        // Assert
        assertThat(foundSurface.getId()).isEqualTo(surfaceType.getId());
        assertThat(foundSurface.getName()).isEqualTo(SurfaceTypeFactory.createSurfaceType().getName());
    }

    @Test
    @Transactional
    void find_surfaceNotFound_successfullyReturnedNull() {
        // Act
        SurfaceType foundSurface = surfaceTypeRepository.find(1);

        // Assert
        assertThat(foundSurface).isEqualTo(null);
    }

    @Test
    @Transactional
    void findAll_twoSurfacesFound_successfullyReturnedSurfaces() {
        // Arrange
        entityManager.persist(SurfaceTypeFactory.createSurfaceType());
        SurfaceType surfaceType = SurfaceTypeFactory.createSurfaceType();
        surfaceType.setName("Antuka");
        entityManager.persist(surfaceType);

        // Act
        List<SurfaceType> foundSurfaces = surfaceTypeRepository.findAll();

        // Assert
        assertThat(foundSurfaces.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    void create_surfaceCreatedSuccessfully_returnCreatedSurface() {
        // Arrange
        SurfaceType surfaceType = SurfaceTypeFactory.createSurfaceType();

        // Act
        SurfaceType surface = surfaceTypeRepository.create(surfaceType);

        // Assert
        assertThat(surface).isEqualTo(surfaceType);
        assertThat(surfaceTypeRepository.find(surface.getId())).isEqualTo(surfaceType);
    }

    @Test
    @Transactional
    void update_surfaceUpdatedSuccessfully_returnUpdatedSurface() {
        // Arrange
        SurfaceType surfaceType = SurfaceTypeFactory.createSurfaceType();
        entityManager.persist(surfaceType);
        entityManager.detach(surfaceType);

        SurfaceType surfaceUpdate = new SurfaceType();
        surfaceUpdate.setId(surfaceType.getId());
        surfaceUpdate.setName("NewName");
        surfaceUpdate.setCostPerMinute(BigDecimal.ONE);

        // Act
        SurfaceType surface = surfaceTypeRepository.update(surfaceUpdate);

        // Assert
        assertThat(surface).isEqualTo(surfaceUpdate);
        assertThat(surfaceTypeRepository.find(surface.getId()).getName()).isEqualTo("NewName");
    }

    @Test
    @Transactional
    void delete_surfaceDeletedSuccessfully_returnDeletedSurface() {
        // Arrange
        SurfaceType surfaceType = SurfaceTypeFactory.createSurfaceType();
        entityManager.persist(surfaceType);
        entityManager.detach(surfaceType);

        // Act
        SurfaceType surface = surfaceTypeRepository.delete(surfaceType);

        // Assert
        assertThat(surface).isEqualTo(surfaceType);
        assertThat(surfaceTypeRepository.find(surface.getId())).isEqualTo(surfaceType);
        assertThat(surface.getDeletedAt()).isNotNull();
    }
}
