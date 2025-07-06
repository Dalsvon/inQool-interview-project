package cz.svonavec.tennis.factory;

import cz.svonavec.tennis.models.dtos.CourtDTO;
import cz.svonavec.tennis.models.entities.Court;
import cz.svonavec.tennis.models.entities.SurfaceType;

import java.time.LocalDateTime;

import static cz.svonavec.tennis.factory.SurfaceTypeFactory.createSurfaceType;
import static cz.svonavec.tennis.factory.SurfaceTypeFactory.createSurfaceTypeDTO;

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
}
