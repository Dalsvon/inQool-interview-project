package cz.svonavec.tennis.facade;

import cz.svonavec.tennis.mapper.SurfaceTypeMapper;
import cz.svonavec.tennis.models.dtos.SurfaceTypeCreateDTO;
import cz.svonavec.tennis.models.dtos.SurfaceTypeDTO;
import cz.svonavec.tennis.service.SurfaceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InvalidObjectException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class SurfaceTypeFacade {
    SurfaceTypeMapper surfaceTypeMapper;
    SurfaceTypeService surfaceTypeService;

    @Autowired
    public SurfaceTypeFacade(SurfaceTypeMapper mapper, SurfaceTypeService service) {
        this.surfaceTypeMapper = mapper;
        this.surfaceTypeService = service;
    }

    @Transactional(readOnly = true)
    public SurfaceTypeDTO findById(long id) {
        return surfaceTypeMapper.mapToDTO(surfaceTypeService.findById(id));
    }

    @Transactional(readOnly = true)
    public List<SurfaceTypeDTO> findAll() {
        return surfaceTypeMapper.mapToDTOList(surfaceTypeService.findAll());
    }

    @Transactional
    public SurfaceTypeDTO create(SurfaceTypeCreateDTO dto) throws InvalidObjectException {
        return surfaceTypeMapper.mapToDTO(surfaceTypeService.create(surfaceTypeMapper.mapToEntity(dto)));
    }

    @Transactional
    public SurfaceTypeDTO update(long id, BigDecimal cost, String name) {
        return surfaceTypeMapper.mapToDTO(surfaceTypeService.update(id, cost, name));
    }

    @Transactional
    public SurfaceTypeDTO delete(long id) {
        return surfaceTypeMapper.mapToDTO(surfaceTypeService.delete(id));
    }
}
