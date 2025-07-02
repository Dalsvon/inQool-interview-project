package cz.svonavec.tennis.facade;

import cz.svonavec.tennis.mapper.SurfaceTypeMapper;
import cz.svonavec.tennis.models.dtos.SurfaceTypeCreateDTO;
import cz.svonavec.tennis.models.dtos.SurfaceTypeDTO;
import cz.svonavec.tennis.service.SurfaceTypeService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SurfaceTypeFacadeTests {
    @Mock
    private SurfaceTypeService surfaceTypeService;

    @Mock
    private SurfaceTypeMapper surfaceTypeMapper;

    @InjectMocks
    private SurfaceTypeFacade surfaceTypeFacade;

    @Test
    void findById_findsSurface_returnsSurface() {
        // Arrange
        when(surfaceTypeService.findById(1)).thenReturn(SurfaceTypeFactory.createSurfaceType());
        when(surfaceTypeMapper.mapToDTO(SurfaceTypeFactory.createSurfaceType())).thenReturn(SurfaceTypeFactory.createSurfaceTypeDTO());

        // Act
        SurfaceTypeDTO result = surfaceTypeFacade.findById(1);

        // Assert
        verify(surfaceTypeService).findById(1);
        assertThat(result.getName()).isEqualTo(SurfaceTypeFactory.createSurfaceType().getName());
    }

    @Test
    void findAll_findsAllSurfaces_returnsSurfaces() {
        // Arrange
        List<SurfaceType> surfaceTypes = List.of(SurfaceTypeFactory.createSurfaceType(), SurfaceTypeFactory.createSurfaceType());
        List<SurfaceTypeDTO> surfaceTypeDTOs = List.of(SurfaceTypeFactory.createSurfaceTypeDTO(), SurfaceTypeFactory.createSurfaceTypeDTO());
        when(surfaceTypeService.findAll()).thenReturn(surfaceTypes);
        when(surfaceTypeMapper.mapToDTOList(surfaceTypes)).thenReturn(surfaceTypeDTOs);

        // Act
        List<SurfaceTypeDTO> result = surfaceTypeFacade.findAll();

        // Assert
        verify(surfaceTypeService).findAll();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void create_createsSurface_returnsSurface() {
        // Arrange
        SurfaceTypeCreateDTO surfaceTypeCreateDTO = new SurfaceTypeCreateDTO();
        surfaceTypeCreateDTO.setName("Grass");
        surfaceTypeCreateDTO.setCostPerMinute(BigDecimal.ONE);
        when(surfaceTypeService.create(SurfaceTypeFactory.createSurfaceType())).thenReturn(SurfaceTypeFactory.createSurfaceType());
        when(surfaceTypeMapper.mapToDTO(SurfaceTypeFactory.createSurfaceType())).thenReturn(SurfaceTypeFactory.createSurfaceTypeDTO());
        when(surfaceTypeMapper.mapToEntity(surfaceTypeCreateDTO)).thenReturn(SurfaceTypeFactory.createSurfaceType());

        // Act
        SurfaceTypeDTO result = surfaceTypeFacade.create(surfaceTypeCreateDTO);

        // Assert
        verify(surfaceTypeService).create(SurfaceTypeFactory.createSurfaceType());
        assertThat(result.getName()).isEqualTo("Grass");
    }

    @Test
    void update_updatesSurface_returnsSurface() {
        // Arrange
        when(surfaceTypeService.update(1, BigDecimal.ONE, "NewName")).thenReturn(SurfaceTypeFactory.createSurfaceType());
        when(surfaceTypeMapper.mapToDTO(SurfaceTypeFactory.createSurfaceType())).thenReturn(SurfaceTypeFactory.createSurfaceTypeDTO());

        // Act
        surfaceTypeFacade.update(1, BigDecimal.ONE, "NewName");

        // Assert
        verify(surfaceTypeService).update(1, BigDecimal.ONE, "NewName");
    }

    @Test
    void delete_deleteSurface_returnsSurface() {
        // Arrange
        SurfaceType surfaceType = SurfaceTypeFactory.createSurfaceType();
        surfaceType.setDeletedAt(LocalDateTime.now());
        when(surfaceTypeService.delete(1)).thenReturn(surfaceType);
        when(surfaceTypeMapper.mapToDTO(surfaceType)).thenReturn(SurfaceTypeFactory.createSurfaceTypeDTO());

        // Act
        surfaceTypeFacade.delete(1);

        // Assert
        verify(surfaceTypeService).delete(1);
    }
}
