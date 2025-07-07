package cz.svonavec.tennis.factory;

import cz.svonavec.tennis.models.dtos.CourtCreateDTO;
import cz.svonavec.tennis.models.dtos.CourtDTO;
import cz.svonavec.tennis.models.entities.Court;
import cz.svonavec.tennis.models.entities.SurfaceType;

import java.time.LocalDateTime;

import static cz.svonavec.tennis.factory.SurfaceTypeFactory.*;

public class CourtFactory {
    public static Court createCourt() {
        Court court = new Court();
        court.setDescription("Standard tennis court");
        court.setSurface(createSurfaceType());
        court.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));
        return court;
    }

    public static Court createCourt(SurfaceType surfaceType) {
        Court court = new Court();
        court.setDescription("Standard tennis court");
        court.setSurface(surfaceType);
        court.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));
        return court;
    }

    public static Court createCourt(String desc, SurfaceType surfaceType) {
        Court court = new Court();
        court.setDescription(desc);
        court.setSurface(surfaceType);
        court.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));
        return court;
    }

    public static CourtDTO createCourtDTO() {
        CourtDTO court = new CourtDTO();
        court.setDescription("Standard tennis court");
        court.setSurface(createSurfaceTypeDTO());
        court.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));
        return court;
    }

    public static CourtDTO createCourtDTORest() {
        CourtDTO court = new CourtDTO();
        court.setId(1L);
        court.setDescription("Standard tennis court");
        court.setSurface(createSurfaceTypeDTORest());
        court.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));
        return court;
    }

    public static CourtCreateDTO createCourtCreateDTORest() {
        CourtCreateDTO court = new CourtCreateDTO();
        court.setDescription("Standard tennis court");
        court.setSurfaceId(1L);
        return court;
    }
}
