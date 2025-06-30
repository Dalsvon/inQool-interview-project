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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class SurfaceRepositoryTests {

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
}
