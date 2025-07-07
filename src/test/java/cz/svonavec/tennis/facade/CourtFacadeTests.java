package cz.svonavec.tennis.facade;

import cz.svonavec.tennis.factory.CourtFactory;
import cz.svonavec.tennis.mapper.CourtMapper;
import cz.svonavec.tennis.models.dtos.CourtCreateDTO;
import cz.svonavec.tennis.models.dtos.CourtDTO;
import cz.svonavec.tennis.models.entities.Court;
import cz.svonavec.tennis.service.CourtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourtFacadeTests {
    @Mock
    private CourtMapper courtMapper;

    @Mock
    private CourtService courtService;

    @InjectMocks
    private CourtFacade courtFacade;

    @Test
    void findById_findsCourt_returnsCourt() {
        // Arrange
        when(courtService.findById(1L)).thenReturn(CourtFactory.createCourt());
        when(courtMapper.mapToDTO(CourtFactory.createCourt())).thenReturn(CourtFactory.createCourtDTO());

        // Act
        CourtDTO courtDTO = courtFacade.findById(1L);

        // Assert
        verify(courtService).findById(1);
        assertThat(courtDTO.getDescription()).isEqualTo(CourtFactory.createCourt().getDescription());
    }

    @Test
    void findAll_findsAllCourts_returnsCourts() {
        // Arrange
        List<Court> courts = List.of(CourtFactory.createCourt(), CourtFactory.createCourt());
        List<CourtDTO> courtDTOs = List.of(CourtFactory.createCourtDTO(), CourtFactory.createCourtDTO());
        when(courtService.findAll()).thenReturn(courts);
        when(courtMapper.mapToDTOList(courts)).thenReturn(courtDTOs);

        // Act
        List<CourtDTO> result = courtFacade.findAll();

        // Assert
        verify(courtService).findAll();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void create_createsCourt_returnsCourt() {
        // Arrange
        CourtCreateDTO courtCreateDTO = new CourtCreateDTO();
        courtCreateDTO.setSurfaceId(1L);
        courtCreateDTO.setDescription("Standard tennis court");
        Court court = new Court();
        court.setDescription("Standard tennis court");
        court.setId(0);
        when(courtService.create(court, courtCreateDTO.getSurfaceId())).thenReturn(CourtFactory.createCourt());
        when(courtMapper.mapToDTO(CourtFactory.createCourt())).thenReturn(CourtFactory.createCourtDTO());
        when(courtMapper.mapToEntity(courtCreateDTO)).thenReturn(court);

        // Act
        CourtDTO result = courtFacade.create(courtCreateDTO);

        // Assert
        verify(courtService).create(court, courtCreateDTO.getSurfaceId());
        assertThat(result.getDescription()).isEqualTo("Standard tennis court");
    }

    @Test
    void update_updatesCourt_returnsCourt() {
        // Arrange
        when(courtService.update(1L, "Standard tennis court", 1L)).thenReturn(CourtFactory.createCourt());
        when(courtMapper.mapToDTO(CourtFactory.createCourt())).thenReturn(CourtFactory.createCourtDTO());

        // Act
        CourtDTO courtDTO = courtFacade.update(1L, "Standard tennis court", 1L);

        // Assert
        verify(courtService).update(1L, "Standard tennis court", 1L);
        assertThat(courtDTO.getDescription()).isEqualTo("Standard tennis court");
    }

    @Test
    void delete_deleteCourt_returnsCourt() {
        // Arrange
        LocalDateTime time = LocalDateTime.now();
        Court court = CourtFactory.createCourt();
        court.setDeletedAt(time);
        when(courtService.delete(1L)).thenReturn(court);
        CourtDTO courtDTO = CourtFactory.createCourtDTO();
        courtDTO.setDeletedAt(time);
        when(courtMapper.mapToDTO(court)).thenReturn(courtDTO);

        // Act
        CourtDTO courtDTOFound = courtFacade.delete(1);

        // Assert
        verify(courtService).delete(1);
        assertThat(courtDTOFound.getDeletedAt()).isNotNull();
        assertThat(courtDTOFound).isEqualTo(courtDTO);
    }
}
