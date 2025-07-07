package cz.svonavec.tennis.service;

import cz.svonavec.tennis.exception.BadRequestException;
import cz.svonavec.tennis.exception.ResourceNotFoundException;
import cz.svonavec.tennis.factory.CourtFactory;
import cz.svonavec.tennis.factory.SurfaceTypeFactory;
import cz.svonavec.tennis.models.entities.Court;
import cz.svonavec.tennis.models.entities.SurfaceType;
import cz.svonavec.tennis.repository.CourtRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourtServiceTests {
    @Mock
    private CourtRepository courtRepository;

    @Mock
    private SurfaceTypeService surfaceTypeService;

    @InjectMocks
    private CourtService courtService;

    @Test
    void find_courtFound_returnsCourt() {
        // Arrange
        when(courtRepository.find(1L)).thenReturn(CourtFactory.createCourt());

        // Act
        Court court = courtService.findById(1L);

        // Assert
        assertThat(court).isEqualTo(CourtFactory.createCourt());
        assertThat(court.getDescription()).isEqualTo("Standard tennis court");
    }

    @Test
    void find_courtNotFound_throwsResourceNotFoundException() {
        assertThrows(ResourceNotFoundException.class, () -> courtService.findById(1));
    }

    @Test
    void findAll_twoCourtsFound_returnsCourts() {
        SurfaceType surfaceType = SurfaceTypeFactory.createSurfaceType();
        surfaceType.setName("Hard");
        surfaceType.setCostPerMinute(BigDecimal.ONE);

        List<Court> courts = List.of(CourtFactory.createCourt(), CourtFactory.createCourt(surfaceType));
        when(courtRepository.findAll()).thenReturn(courts);

        // Act
        List<Court> courtsFound = courtService.findAll();

        // Assert
        assertThat(courtsFound.size()).isEqualTo(2);
        assertThat(courtsFound.getFirst().getSurface().getName()).isEqualTo("Grass");
        assertThat(courtsFound.get(1).getSurface().getName()).isEqualTo("Hard");
    }

    @Test
    void create_courtCreated_returnsCourt() {
        // Arrange
        Court court = CourtFactory.createCourt();
        court.setId(0L);
        court.setSurface(null);
        when(courtRepository.create(court)).thenReturn(CourtFactory.createCourt());
        when(surfaceTypeService.findById(1L)).thenReturn(SurfaceTypeFactory.createSurfaceType());

        // Act
        Court courtFound = courtService.create(court, 1L);

        // Assert
        assertThat(courtFound).isEqualTo(CourtFactory.createCourt());
        assertThat(court.getDescription()).isEqualTo("Standard tennis court");
        verify(courtRepository).create(court);
        verify(surfaceTypeService).findById(1L);
    }

    @Test
    void create_courtHasId_throwsthrowsBadRequestException() {
        Court court = CourtFactory.createCourt();
        court.setId(1L);
        assertThrows(BadRequestException.class, () -> courtService.create(court, 1L));
    }

    @Test
    void update_courtUpdated_returnsCourt() {
        // Arrange
        SurfaceType surfaceTypeUpdate = SurfaceTypeFactory.createSurfaceType();
        surfaceTypeUpdate.setName("NewName");
        surfaceTypeUpdate.setId(1L);

        when(courtRepository.find(1)).thenReturn(CourtFactory.createCourt());
        Court update = CourtFactory.createCourt();
        update.setDescription("New desc");
        update.setSurface(surfaceTypeUpdate);
        when(courtRepository.update(update)).thenReturn(update);

        // Act
        Court court = courtService.update(1L, "New desc", surfaceTypeUpdate.getId());

        // Assert
        assertThat(court).isEqualTo(update);
        assertThat(court.getDescription()).isEqualTo("New desc");
        assertThat(court.getSurface().getName()).isEqualTo("NewName");
        verify(courtRepository).update(update);
    }

    @Test
    void update_noUpdateFields_throwsBadRequestException() {
        // Arrange
        when(courtRepository.find(1)).thenReturn(CourtFactory.createCourt());

        // Act
        assertThrows(BadRequestException.class, () -> courtService.update(1, null, null));
    }

    @Test
    void delete_courtDeleted_returnsCourt() {
        // Arrange
        SurfaceType surfaceType = SurfaceTypeFactory.createSurfaceType();
        Court courtDeleted = CourtFactory.createCourt();
        courtDeleted.setId(1L);
        courtDeleted.setDeletedAt(LocalDateTime.now());
        courtDeleted.setSurface(surfaceType);


        when(courtRepository.find(1L)).thenReturn(CourtFactory.createCourt());
        when(courtRepository.delete(CourtFactory.createCourt())).thenReturn(courtDeleted);

        // Act
        Court court = courtService.delete(1L);

        // Assert
        assertThat(court.getDeletedAt()).isNotNull();
        assertThat(court.getId()).isEqualTo(1L);
        verify(courtRepository).find(1L);
        verify(courtRepository).delete(CourtFactory.createCourt());
    }
}
