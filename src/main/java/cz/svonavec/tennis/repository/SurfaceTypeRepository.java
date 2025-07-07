package cz.svonavec.tennis.repository;

import cz.svonavec.tennis.models.entities.SurfaceType;

import java.util.List;

public interface SurfaceTypeRepository {
    /**
     * Finds and returns SurfaceType from the database with corresponding id
     *
     * @param id id of the SurfaceType
     * @return found SurfaceType
     */
    SurfaceType find(long id);

    /**
     * Finds and returns all surfaces in the database (undeleted)
     *
     * @return all surfaces
     */
    List<SurfaceType> findAll();

    /**
     * Updates the surface with given data (changes information in surface with the same id) in the database
     *
     * @param surfaceType surface data
     * @return updated surface
     */
    SurfaceType update(SurfaceType surfaceType);

    /**
     * Creates and saves surface from given data
     *
     * @param surfaceType surface data
     * @return created surface type
     */
    SurfaceType create(SurfaceType surfaceType);

    /**
     * Deletes surface from the database. Performed as SOFT delete by assigning deletedAt field
     *
     * @param surfaceType surface data
     * @return deleted surface type
     */
    SurfaceType delete(SurfaceType surfaceType);
}
