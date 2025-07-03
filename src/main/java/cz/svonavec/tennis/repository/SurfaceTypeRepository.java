package cz.svonavec.tennis.repository;

import cz.svonavec.tennis.models.entities.SurfaceType;

import java.util.List;

public interface SurfaceTypeRepository {
    SurfaceType find(long id);
    List<SurfaceType> findAll();
    SurfaceType update(SurfaceType court);
    SurfaceType create(SurfaceType court);
    SurfaceType delete(SurfaceType surfaceType);
}
