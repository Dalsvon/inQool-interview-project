package cz.svonavec.tennis.repository;

import cz.svonavec.tennis.models.entities.Court;

import java.util.List;

public interface CourtRepository {
    /**
     * Finds and returns court from the database with corresponding id
     *
     * @param id id of the court
     * @return found court
     */
    Court find(long id);

    /**
     * Finds and returns all courts in the database (undeleted)
     *
     * @return all courts
     */
    List<Court> findAll();

    /**
     * Updates the court with given data (changes information in court with the same id) in the database
     *
     * @param court court data
     * @return updated court
     */
    Court update(Court court);

    /**
     * Creates and saves court from given data
     *
     * @param court court data from
     * @return created court with id
     */
    Court create(Court court);

    /**
     * Deletes court from the database. Performed as SOFT delete by assigning deletedAt field
     *
     * @param court court to be deleted
     * @return deleted court
     */
    Court delete(Court court);
}
