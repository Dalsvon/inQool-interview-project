package cz.svonavec.tennis.mapper;

import cz.svonavec.tennis.factory.SurfaceTypeFactory;
import cz.svonavec.tennis.models.dtos.SurfaceTypeCreateDTO;
import cz.svonavec.tennis.models.dtos.SurfaceTypeDTO;
import cz.svonavec.tennis.models.entities.SurfaceType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class SurfaceTypeMapperTests {

    @Autowired
    private SurfaceTypeMapper surfaceTypeMapper;

    @Test
    void mapToEntity_correctCreateDTO_successfullyMapsToSurfaceTypeEntity() {
        // Arrange
        SurfaceTypeCreateDTO createDTO = new SurfaceTypeCreateDTO();
        createDTO.setName("Clay");
        createDTO.setCostPerMinute(BigDecimal.valueOf(1.8));

        // Act
        SurfaceType mappedEntity = surfaceTypeMapper.mapToEntity(createDTO);

        // Assert
        assertThat(mappedEntity).isNotNull();
        assertThat(mappedEntity.getId()).isEqualTo(0);
        assertThat(mappedEntity.getName()).isEqualTo("Clay");
        assertThat(mappedEntity.getCostPerMinute()).isEqualTo(BigDecimal.valueOf(1.8));
    }

    @Test
    void mapToDTO_fromSurfaceTypeEntity_successfullyMapsToSurfaceTypeDTO() {
        // Arrange
        SurfaceType entity = SurfaceTypeFactory.createSurfaceType();
        entity.setId(1L);

        LocalDateTime createdAt = LocalDateTime.now().plusDays(2);
        LocalDateTime deletedAt = LocalDateTime.now().plusDays(5);
        entity.setCreatedAt(createdAt);
        entity.setDeletedAt(deletedAt);

        // Act
        SurfaceTypeDTO mappedDTO = surfaceTypeMapper.mapToDTO(entity);

        // Assert
        assertThat(mappedDTO).isNotNull();
        assertThat(mappedDTO.getId()).isEqualTo(1L);
        assertThat(mappedDTO.getName()).isEqualTo("Grass");
        assertThat(mappedDTO.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void mapToDTOList_fromSurfaceTypeEntityList_successfullyMapsToSurfaceTypeDTOList() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.now().plusDays(2);
        SurfaceType surfaceType1 = SurfaceTypeFactory.createSurfaceType();
        surfaceType1.setId(1L);
        surfaceType1.setCreatedAt(createdAt);

        SurfaceType surfaceType2 = SurfaceTypeFactory.createSurfaceType();
        surfaceType2.setId(2L);
        surfaceType2.setCreatedAt(createdAt);

        List<SurfaceType> entities = List.of(surfaceType1, surfaceType2);

        // Act
        List<SurfaceTypeDTO> mappedDTOs = surfaceTypeMapper.mapToDTOList(entities);

        // Assert
        assertThat(mappedDTOs).isNotNull();
        assertThat(mappedDTOs.size()).isEqualTo(2);

        // Verify first surface (Grass)
        SurfaceTypeDTO grassDTO = mappedDTOs.get(0);
        assertThat(grassDTO.getId()).isEqualTo(1L);
        assertThat(grassDTO.getName()).isEqualTo("Grass");
        assertThat(grassDTO.getDeletedAt()).isNull();
    }
}
