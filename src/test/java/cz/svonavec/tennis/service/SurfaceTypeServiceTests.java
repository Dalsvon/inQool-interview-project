package cz.svonavec.tennis.service;

import cz.svonavec.tennis.exception.BadRequestException;
import cz.svonavec.tennis.exception.ResourceNotFoundException;
import cz.svonavec.tennis.factory.SurfaceTypeFactory;
import cz.svonavec.tennis.models.entities.SurfaceType;
import cz.svonavec.tennis.repository.SurfaceTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SurfaceTypeServiceTests {
    @Mock
    private SurfaceTypeRepository surfaceTypeRepository;

    @InjectMocks
    private SurfaceTypeService surfaceTypeService;

    @Test
    void find_surfaceFound_returnsSurface() {
        // Arrange
        when(surfaceTypeRepository.find(1)).thenReturn(SurfaceTypeFactory.createSurfaceType());

        // Act
        SurfaceType surfaceType = surfaceTypeService.findById(1);

        // Assert
        assertThat(surfaceType).isEqualTo(SurfaceTypeFactory.createSurfaceType());
    }

    @Test
    void find_surfaceNotFound_throwsResourceNotFoundException() {
        assertThrows(ResourceNotFoundException.class, () -> surfaceTypeService.findById(1));
    }

    @Test
    void findAll_twoSurfacesFound_returnsSurfaces() {
        SurfaceType surfaceType = SurfaceTypeFactory.createSurfaceType();
        surfaceType.setName("Hard");

        List<SurfaceType> surfaceTypeList = List.of(surfaceType, SurfaceTypeFactory.createSurfaceType());
        when(surfaceTypeRepository.findAll()).thenReturn(surfaceTypeList);

        // Act
        List<SurfaceType> surfaceTypes = surfaceTypeService.findAll();

        // Assert
        assertThat(surfaceTypes.size()).isEqualTo(2);
        assertThat(surfaceTypes.getFirst().getName()).isEqualTo("Hard");
    }

    @Test
    void create_surfaceCreated_returnsSurface() {
        // Arrange
        SurfaceType surfaceTypeCreate = SurfaceTypeFactory.createSurfaceType();
        surfaceTypeCreate.setId(1);

        when(surfaceTypeRepository.create(SurfaceTypeFactory.createSurfaceType())).thenReturn(surfaceTypeCreate);

        // Act
        SurfaceType surfaceType = surfaceTypeService.create(SurfaceTypeFactory.createSurfaceType());

        // Assert
        assertThat(surfaceType).isEqualTo(SurfaceTypeFactory.createSurfaceType());
        assertThat(surfaceType.getId()).isEqualTo(1);
    }

    @Test
    void create_surfaceHasId_throwsBadRequestException() {
        SurfaceType surfaceType = SurfaceTypeFactory.createSurfaceType();
        surfaceType.setName("Hard");
        surfaceType.setId(1L);

        assertThrows(BadRequestException.class, () -> surfaceTypeService.create(surfaceType));
    }

    @Test
    void update_surfaceUpdated_returnsSurface() {
        // Arrange
        SurfaceType surfaceTypeUpdate = SurfaceTypeFactory.createSurfaceType();
        surfaceTypeUpdate.setName("NewName");

        when(surfaceTypeRepository.find(1)).thenReturn(SurfaceTypeFactory.createSurfaceType());
        when(surfaceTypeRepository.update(surfaceTypeUpdate)).thenReturn(surfaceTypeUpdate);

        // Act
        SurfaceType surfaceType = surfaceTypeService.update(1, null, "NewName");

        // Assert
        assertThat(surfaceType).isEqualTo(surfaceTypeUpdate);
        assertThat(surfaceType.getName()).isEqualTo("NewName");
    }

    @Test
    void update_noUpdateFields_throwsBadRequestException() {
        // Arrange
        when(surfaceTypeRepository.find(1)).thenReturn(SurfaceTypeFactory.createSurfaceType());

        // Act
        assertThrows(BadRequestException.class, () -> surfaceTypeService.update(1, null, null));
    }

    @Test
    void delete_surfaceDeleted_returnsSurface() {
        // Arrange
        SurfaceType surfaceTypeDeleted = SurfaceTypeFactory.createSurfaceType();
        surfaceTypeDeleted.setDeletedAt(LocalDateTime.now());

        when(surfaceTypeRepository.find(1)).thenReturn(SurfaceTypeFactory.createSurfaceType());
        when(surfaceTypeRepository.delete(SurfaceTypeFactory.createSurfaceType())).thenReturn(surfaceTypeDeleted);

        // Act
        SurfaceType surfaceType = surfaceTypeService.delete(1);

        // Assert
        assertThat(surfaceType.getDeletedAt()).isNotNull();
    }
}
