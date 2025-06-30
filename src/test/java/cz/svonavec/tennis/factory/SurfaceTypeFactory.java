package cz.svonavec.tennis.factory;

import cz.svonavec.tennis.models.entities.SurfaceType;

import java.math.BigDecimal;

public class SurfaceTypeFactory {
    public static SurfaceType createSurfaceType() {
        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setCostPerMinute(BigDecimal.ONE);
        surfaceType.setName("Grass");

        return surfaceType;
    }
}
