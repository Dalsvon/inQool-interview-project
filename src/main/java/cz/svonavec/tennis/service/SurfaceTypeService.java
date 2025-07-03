package cz.svonavec.tennis.service;

import cz.svonavec.tennis.exception.BadRequestException;
import cz.svonavec.tennis.exception.ResourceNotFoundException;
import cz.svonavec.tennis.models.entities.SurfaceType;
import cz.svonavec.tennis.repository.SurfaceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SurfaceTypeService {
    public final SurfaceTypeRepository surfaceTypeRepository;

    @Autowired
    public SurfaceTypeService(SurfaceTypeRepository surfaceTypeRepository){
        this.surfaceTypeRepository = surfaceTypeRepository;
    }

    @Transactional(readOnly = true)
    public SurfaceType findById(long id) {
        SurfaceType type = surfaceTypeRepository.find(id);
        if (type == null || type.getDeletedAt() != null) {
            throw new ResourceNotFoundException("Couldn't find surface type with this id.");
        }
        return type;
    }

    @Transactional(readOnly = true)
    public List<SurfaceType> findAll() {
        return surfaceTypeRepository.findAll();
    }

    @Transactional
    public SurfaceType create(SurfaceType surfaceType) {
        if (surfaceType.getId() != 0) {
            throw new BadRequestException("Trying to create a surface with set id.");
        }
        return surfaceTypeRepository.create(surfaceType);
    }

    @Transactional
    public SurfaceType update(long id, BigDecimal cost, String name) {
        SurfaceType surfaceType = findById(id);
        if (cost != null || name != null) {
            if (cost != null) {
                surfaceType.setCostPerMinute(cost);
            }
            if (name != null) {
                surfaceType.setName(name);
            }
        } else {
            throw new BadRequestException("At least one query field must be used.");
        }
        return surfaceTypeRepository.update(surfaceType);
    }

    @Transactional
    public SurfaceType delete(long id) {
        SurfaceType surfaceType = findById(id);
        return surfaceTypeRepository.delete(surfaceType);
    }
}
