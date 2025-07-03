package cz.svonavec.tennis.mapper;

import cz.svonavec.tennis.models.dtos.CourtCreateDTO;
import cz.svonavec.tennis.models.dtos.CourtDTO;
import cz.svonavec.tennis.models.entities.Court;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourtMapper {

    private final SurfaceTypeMapper surfaceTypeMapper;

    @Autowired
    public CourtMapper(SurfaceTypeMapper surfaceTypeMapper) {
        this.surfaceTypeMapper = surfaceTypeMapper;
    }

    public Court mapToEntity(CourtCreateDTO dto) {
        Court court = new Court();

        court.setDescription(dto.getDescription());
        court.setId(0);

        return court;
    }

    public CourtDTO mapToDTO(Court court) {
        CourtDTO dto = new CourtDTO();

        dto.setId(court.getId());
        dto.setDescription(court.getDescription());
        dto.setDeletedAt(court.getDeletedAt());
        dto.setCreatedAt(court.getCreatedAt());
        dto.setSurface(surfaceTypeMapper.mapToDTO(court.getSurface()));

        return dto;
    }

    public List<CourtDTO> mapToDTOList(List<Court> courts) {
        List<CourtDTO> DTOs = new ArrayList<>();
        for (Court court : courts) {
            DTOs.add(mapToDTO(court));
        }
        return DTOs;
    }
}
