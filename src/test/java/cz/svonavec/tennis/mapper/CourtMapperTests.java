package cz.svonavec.tennis.mapper;

import cz.svonavec.tennis.factory.CourtFactory;
import cz.svonavec.tennis.factory.SurfaceTypeFactory;
import cz.svonavec.tennis.models.dtos.CourtCreateDTO;
import cz.svonavec.tennis.models.dtos.CourtDTO;
import cz.svonavec.tennis.models.dtos.SurfaceTypeDTO;
import cz.svonavec.tennis.models.entities.Court;
import cz.svonavec.tennis.models.entities.SurfaceType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourtMapperTests {
    @Mock
    private SurfaceTypeMapper surfaceTypeMapper;

    @InjectMocks
    private CourtMapper courtMapper;

    @Test
    void mapToEntity_correctDTO_successfullyMapsToCourtEntity() {
        // Arrange
        CourtCreateDTO createDTO = new CourtCreateDTO();
        createDTO.setDescription("desc");
        createDTO.setSurfaceId(1L);

        // Act
        Court mappedCourt = courtMapper.mapToEntity(createDTO);

        // Assert
        assertThat(mappedCourt).isNotNull();
        assertThat(mappedCourt.getId()).isEqualTo(0);
        assertThat(mappedCourt.getDescription()).isEqualTo("desc");
        assertThat(mappedCourt.getSurface()).isNull();
        assertThat(mappedCourt.getCreatedAt()).isNull();
        assertThat(mappedCourt.getDeletedAt()).isNull();
    }

    @Test
    void mapToDTO_correctEntity_successfullyMapsToCourtDTO() {
        // Arrange
        SurfaceType surfaceType = SurfaceTypeFactory.createSurfaceType();
        surfaceType.setId(1L);

        Court court = CourtFactory.createCourt("Clay", surfaceType);
        court.setId(5L);

        LocalDateTime createdAt = LocalDateTime.now().minusDays(2);
        LocalDateTime deletedAt = LocalDateTime.now().minusDays(5);
        court.setCreatedAt(createdAt);
        court.setDeletedAt(deletedAt);

        SurfaceTypeDTO surfaceTypeDTO = new SurfaceTypeDTO();
        surfaceTypeDTO.setId(1L);
        surfaceTypeDTO.setName("Clay");
        surfaceTypeDTO.setCostPerMinute(BigDecimal.valueOf(1.5));

        when(surfaceTypeMapper.mapToDTO(surfaceType)).thenReturn(surfaceTypeDTO);

        // Act
        CourtDTO mappedDTO = courtMapper.mapToDTO(court);

        // Assert
        assertThat(mappedDTO).isNotNull();
        assertThat(mappedDTO.getId()).isEqualTo(5L);
        assertThat(mappedDTO.getDescription()).isEqualTo("Clay");
        assertThat(mappedDTO.getCreatedAt()).isEqualTo(createdAt);
        assertThat(mappedDTO.getSurface()).isNotNull();
        assertThat(mappedDTO.getSurface().getId()).isEqualTo(1L);
    }

    @Test
    void mapToDTOList_fromCourtEntityList_successfullyMapsToCourtDTOList() {
        // Arrange
        SurfaceType surfaceType1 = SurfaceTypeFactory.createSurfaceType();
        surfaceType1.setId(1L);

        SurfaceType surfaceType2 = SurfaceTypeFactory.createSurfaceType();
        surfaceType2.setId(2L);
        surfaceType2.setName("Clay");


        Court court1 = CourtFactory.createCourt("Grass court 1", surfaceType1);
        court1.setId(1L);

        Court court2 = CourtFactory.createCourt("Clay court", surfaceType2);
        court2.setId(2L);

        Court court3 = CourtFactory.createCourt("Grass court 1", surfaceType1);
        court3.setId(3L);

        List<Court> courts = List.of(court1, court2, court3);

        SurfaceTypeDTO grassSurfaceDTO = new SurfaceTypeDTO();
        grassSurfaceDTO.setId(1L);
        grassSurfaceDTO.setName("Grass");
        grassSurfaceDTO.setCostPerMinute(BigDecimal.valueOf(2.0));

        SurfaceTypeDTO claySurfaceDTO = new SurfaceTypeDTO();
        claySurfaceDTO.setId(2L);
        claySurfaceDTO.setName("Clay");
        claySurfaceDTO.setCostPerMinute(BigDecimal.valueOf(1.5));

        when(surfaceTypeMapper.mapToDTO(surfaceType1)).thenReturn(grassSurfaceDTO);
        when(surfaceTypeMapper.mapToDTO(surfaceType2)).thenReturn(claySurfaceDTO);

        // Act
        List<CourtDTO> mappedDTOs = courtMapper.mapToDTOList(courts);

        // Assert
        assertThat(mappedDTOs).isNotNull();
        assertThat(mappedDTOs.size()).isEqualTo(3);

        CourtDTO dto1 = mappedDTOs.get(0);
        assertThat(dto1.getId()).isEqualTo(1L);
        assertThat(dto1.getDescription()).isEqualTo("Grass court 1");
        assertThat(dto1.getSurface().getName()).isEqualTo("Grass");

        CourtDTO dto2 = mappedDTOs.get(1);
        assertThat(dto2.getId()).isEqualTo(2L);
        assertThat(dto2.getDescription()).isEqualTo("Clay court");
        assertThat(dto2.getSurface().getName()).isEqualTo("Clay");
    }
}
