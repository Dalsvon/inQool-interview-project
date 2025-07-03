package cz.svonavec.tennis.mapper;

import cz.svonavec.tennis.models.dtos.SurfaceTypeCreateDTO;
import cz.svonavec.tennis.models.dtos.SurfaceTypeDTO;
import cz.svonavec.tennis.models.entities.SurfaceType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SurfaceTypeMapper {
    public SurfaceType mapToEntity(SurfaceTypeDTO dto) {
        SurfaceType type = new SurfaceType();

        type.setName(dto.getName());
        type.setDeletedAt(dto.getDeletedAt());
        type.setCostPerMinute(dto.getCostPerMinute());
        type.setId(dto.getId());

        return type;
    }

    public SurfaceType mapToEntity(SurfaceTypeCreateDTO dto) {
        SurfaceType type = new SurfaceType();

        type.setName(dto.getName());
        type.setDeletedAt(null);
        type.setCostPerMinute(dto.getCostPerMinute());
        type.setId(0);

        return type;
    }

    public SurfaceTypeDTO mapToDTO(SurfaceType type) {
        SurfaceTypeDTO dto = new SurfaceTypeDTO();

        dto.setName(type.getName());
        dto.setDeletedAt(type.getDeletedAt());
        dto.setCostPerMinute(type.getCostPerMinute());
        dto.setCreatedAt(type.getCreatedAt());
        dto.setId(type.getId());

        return dto;
    }

    public List<SurfaceTypeDTO> mapToDTOList(List<SurfaceType> types) {
        List<SurfaceTypeDTO> DTOs = new ArrayList<>();
        for (SurfaceType type : types) {
            DTOs.add(mapToDTO(type));
        }
        return DTOs;
    }
}
