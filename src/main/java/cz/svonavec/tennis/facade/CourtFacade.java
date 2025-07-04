package cz.svonavec.tennis.facade;

import cz.svonavec.tennis.mapper.CourtMapper;
import cz.svonavec.tennis.models.dtos.CourtCreateDTO;
import cz.svonavec.tennis.models.dtos.CourtDTO;
import cz.svonavec.tennis.service.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourtFacade {
    CourtMapper courtMapper;
    CourtService courtService;

    @Autowired
    public CourtFacade(CourtMapper courtMapper, CourtService courtService) {
        this.courtMapper = courtMapper;
        this.courtService = courtService;
    }

    @Transactional(readOnly = true)
    public CourtDTO findById(long id) {
        return courtMapper.mapToDTO(courtService.findById(id));
    }

    @Transactional(readOnly = true)
    public List<CourtDTO> findAll() {
        return courtMapper.mapToDTOList(courtService.findAll());
    }

    @Transactional
    public CourtDTO create(CourtCreateDTO dto) {
        return courtMapper.mapToDTO(courtService.create(courtMapper.mapToEntity(dto), dto.getSurfaceId()));
    }

    @Transactional
    public CourtDTO update(long id, String description, Long surfaceId) {
        return courtMapper.mapToDTO(courtService.update(id, description, surfaceId));
    }

    @Transactional
    public CourtDTO delete(long id) {
        return courtMapper.mapToDTO(courtService.delete(id));
    }
}
