package cz.svonavec.tennis.service;

import cz.svonavec.tennis.exception.BadRequestException;
import cz.svonavec.tennis.exception.ResourceNotFoundException;
import cz.svonavec.tennis.models.entities.Court;
import cz.svonavec.tennis.models.entities.SurfaceType;
import cz.svonavec.tennis.repository.CourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourtService {
    public final CourtRepository courtRepository;

    public final SurfaceTypeService surfaceTypeService;

    @Autowired
    public CourtService(CourtRepository courtRepository, SurfaceTypeService surfaceTypeService){
        this.courtRepository = courtRepository;
        this.surfaceTypeService = surfaceTypeService;
    }

    @Transactional(readOnly = true)
    public Court findById(long id) {
        Court court = courtRepository.find(id);
        if (court == null || court.getDeletedAt() != null) {
            throw new ResourceNotFoundException("Couldn't find court with this id.");
        }
        return court;
    }

    @Transactional(readOnly = true)
    public List<Court> findAll() {
        return courtRepository.findAll();
    }

    @Transactional
    public Court create(Court court, long surfaceTypeId) {
        if (court.getId() != 0) {
            throw new BadRequestException("Trying to create a court with set id.");
        }
        SurfaceType surfaceType = surfaceTypeService.findById(surfaceTypeId);
        court.setSurface(surfaceType);
        return courtRepository.create(court);
    }

    @Transactional
    public Court update(long id, String description, Long surfaceTypeId) {
        Court court = findById(id);
        if (description != null || surfaceTypeId != null) {
            if (description != null) {
                court.setDescription(description);
            }
            if (surfaceTypeId != null) {
                SurfaceType surfaceType = surfaceTypeService.findById(surfaceTypeId);
                court.setSurface(surfaceType);
            }
        } else {
            throw new BadRequestException("At least one query field must be used.");
        }
        return courtRepository.update(court);
    }

    @Transactional
    public Court delete(long id) {
        Court court = findById(id);
        // Add reservation delete
        return courtRepository.delete(court);
    }
}
