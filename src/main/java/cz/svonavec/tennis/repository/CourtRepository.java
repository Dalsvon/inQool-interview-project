package cz.svonavec.tennis.repository;

import cz.svonavec.tennis.models.entities.Court;

import java.math.BigDecimal;
import java.util.List;

public interface CourtRepository {
    Court find(long id);
    List<Court> findAll();
    Court update(Court court);
    Court create(Court court);
    Court delete(Court court);
}
