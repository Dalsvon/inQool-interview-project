package cz.svonavec.tennis.factory;

import cz.svonavec.tennis.models.dtos.SurfaceTypeCreateDTO;
import cz.svonavec.tennis.models.dtos.SurfaceTypeDTO;
import cz.svonavec.tennis.models.entities.SurfaceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SurfaceTypeFactory {
    public static SurfaceType createSurfaceType() {
        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setCostPerMinute(BigDecimal.ONE);
        surfaceType.setName("Grass");
        surfaceType.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));

        return surfaceType;
    }

    public static SurfaceTypeDTO createSurfaceTypeDTO() {
        SurfaceTypeDTO surfaceType = new SurfaceTypeDTO();
        surfaceType.setCostPerMinute(BigDecimal.ONE);
        surfaceType.setName("Grass");
        surfaceType.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));

        return surfaceType;
    }

    public static SurfaceTypeDTO createSurfaceTypeDTORest() {
        SurfaceTypeDTO surfaceType = new SurfaceTypeDTO();
        surfaceType.setId(1L);
        surfaceType.setCostPerMinute(BigDecimal.ONE);
        surfaceType.setName("Grass");
        surfaceType.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));

        return surfaceType;
    }

    public static SurfaceTypeCreateDTO createSurfaceTypeCreateDTORest() {
        SurfaceTypeCreateDTO surfaceType = new SurfaceTypeCreateDTO();
        surfaceType.setCostPerMinute(BigDecimal.ONE);
        surfaceType.setName("Grass");

        return surfaceType;
    }
}
