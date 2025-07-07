package cz.svonavec.tennis.entity;

import cz.svonavec.tennis.factory.CourtFactory;
import cz.svonavec.tennis.models.entities.Reservation;
import cz.svonavec.tennis.models.entities.SurfaceType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ReservationTests {
    @Test
    public void calculateCost_noCourt_setsZero() {
        Reservation reservation = new Reservation();
        reservation.setDoubles(false);
        reservation.setStartsAt(LocalDateTime.of(2025, 1, 1, 10, 0));
        reservation.setEndsAt(LocalDateTime.of(2025, 1, 1, 11, 0));
        reservation.setCourt(null);
        reservation.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));

        reservation.calculateCost();

        assertThat(reservation.getCost()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void calculateCost_hourLongReservation_setsCorrectCost() {
        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setName("Grass");
        surfaceType.setId(1L);
        surfaceType.setCostPerMinute(BigDecimal.valueOf(3.5));


        Reservation reservation = new Reservation();
        reservation.setDoubles(false);
        reservation.setStartsAt(LocalDateTime.of(2025, 1, 1, 10, 0));
        reservation.setEndsAt(LocalDateTime.of(2025, 1, 1, 11, 0));
        reservation.setCost(BigDecimal.valueOf(60.00));
        reservation.setCourt(CourtFactory.createCourt(surfaceType));
        reservation.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));

        reservation.calculateCost();

        assertThat(reservation.getCost()).isEqualTo(BigDecimal.valueOf(210.00).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void calculateCost_doublesReservation_setsCorrectCost() {
        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setName("Grass");
        surfaceType.setId(1L);
        surfaceType.setCostPerMinute(BigDecimal.valueOf(3.5));


        Reservation reservation = new Reservation();
        reservation.setDoubles(true);
        reservation.setStartsAt(LocalDateTime.of(2025, 1, 1, 10, 0));
        reservation.setEndsAt(LocalDateTime.of(2025, 1, 1, 11, 0));
        reservation.setCost(BigDecimal.valueOf(60.00));
        reservation.setCourt(CourtFactory.createCourt(surfaceType));
        reservation.setCreatedAt(LocalDateTime.of(2025, 1, 1, 0, 0));

        reservation.calculateCost();

        assertThat(reservation.getCost()).isEqualTo(BigDecimal.valueOf(315.00).setScale(2, RoundingMode.HALF_UP));
    }
}
